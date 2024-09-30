package com.tsxy.riskcontrol.client.model;

import com.tsxy.riskcontrol.client.util.SecTimeUtil;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:25
 */
public class RCData {

    /*
     * static PrintWriter pw;
     *
     * static { try { pw = new PrintWriter("/tmp/rc_" + UUID.randomUUID().toString()
     * + ".txt"); } catch (FileNotFoundException e) { e.printStackTrace(); }
     *
     * }
     */

    private final String uuid = UUID.randomUUID().toString().replace("-", "");

    /**
     * 该风控记录初始访达时间，当该值等于0时表示异步更新
     */
    private long touchTs;

    private boolean touchTsResetFlag = false;

    private long touchTimes;

    //private long banTimeWindow;

    private long stickKeepEndTime;

    private boolean accessable = true;

    private AtomicLong deltaInc = new AtomicLong();

    private long expiredTs;

    private String baseKey;

    private Set<String> stickTimeSets;

    private RCThreshold lastThreshold;

    private boolean updateStickFlag = false;

    public RCData(String baseKey, long expiredTs) {
        this.baseKey = baseKey;
        this.touchTs = SecTimeUtil.currentSec();
        // 第一次同步时执行reset
        this.touchTsResetFlag = true;
        this.touchTimes = 0;
        this.stickTimeSets = new HashSet<String>();
        this.expiredTs = this.touchTs + expiredTs;
    }

    public void merge(RCData rcDataInCache) {
        this.touchTimes = rcDataInCache.touchTimes;
        Set<String> stickSet = rcDataInCache.stickTimeSets;

        if(stickSet == null) {
            stickSet = new HashSet<String>();
        }
        this.stickTimeSets = stickSet;
    }

    public RCData(String baseKey, long touchTs, int touchTimes, Set<String> stickTimeSets, long expiredTs) {
        this.baseKey = baseKey;
        this.touchTs = touchTs;
        this.touchTimes = touchTimes;
        this.expiredTs = this.touchTs + expiredTs;
        if(stickTimeSets == null) {
            this.stickTimeSets = new HashSet<String>();
        } else {
            this.stickTimeSets = stickTimeSets;
        }
    }

    public boolean isValid() {
        return this.touchTs == 0 || (SecTimeUtil.currentSec() < expiredTs);
    }

    public boolean accessable() {
        return accessable;
    }

    public boolean needUpdateStick() {
        boolean flag = updateStickFlag;
        updateStickFlag = false;
        return flag;
    }

    private boolean expireTimeRefreshFlag = false;
    public boolean needRefreshExpireTime() {
        return expireTimeRefreshFlag;
    }

    private static final long BAN_DEFAULT_DELTA_INC = 10000000L;
    // 被关联ban时，仅当次窗口的
    public void ban(long banEndTs) {

        if(this.accessable) {
            //this.banTimeWindow = banInterval;
            if(lastThreshold == null || this.deltaInc.get() + this.touchTimes < lastThreshold.getThreshold()) {
                this.deltaInc.addAndGet(BAN_DEFAULT_DELTA_INC);
            }

            //if(this.expiredTs < banEndTs) {
            this.expiredTs = banEndTs;
            //}

            this.accessable = false;
            this.expireTimeRefreshFlag = true;
        }
    }

    public RCResult touch(RCThreshold threshold) {
        return touch(threshold, false);
    }

    public RCResult touch(RCThreshold threshold, boolean checkOnly) {

        RCResult rcResult = new RCResult();
        this.lastThreshold = threshold;

        // 仅处理未被禁用的访问
        if(this.accessable) {
            if(!checkOnly) {
                this.deltaInc.incrementAndGet();
            }
            long currentTs = SecTimeUtil.currentSec();
            long banTimeWindow = 0;

            // 本次时间窗口被禁用
            if(this.deltaInc.get() + this.touchTimes > threshold.getThreshold()) {
                banTimeWindow = threshold.getBanInterval();
                this.stickTimeSets.add(String.valueOf(this.touchTs));
                this.stickKeepEndTime = currentTs + threshold.getStickInterval();
                this.accessable = false;
                if(!checkOnly) {
                    updateStickFlag = true;
                }
            }

            long stickThreshold = threshold.getStickThreshold();
            if(stickThreshold > 0 && this.stickTimeSets.size() > threshold.getStickThreshold()) {

                long stickKeepDetlaTime = threshold.getInterval() + threshold.getStickBanInterval() + 1;
                long earlestTs = this.stickKeepEndTime - stickKeepDetlaTime;

                // 严格检查过期时间
                if(this.stickTimeSets.stream().filter(pointTs -> Long.parseLong(pointTs) > earlestTs).count() > threshold.getStickThreshold()) {
                    banTimeWindow = threshold.getStickBanInterval();
                    this.stickKeepEndTime = currentTs + threshold.getStickBanInterval();

                    this.accessable = false;
                }

            }
            if(banTimeWindow > 0) {
                // 忽略redis同步延时产生的时间差不一致
                resetExpireTs(/* checkOnly ? this.touchTs : */currentTs, banTimeWindow);
                rcResult.setBanEndTs(this.getExpireTs());
            }
        }
        rcResult.setAccessAble(this.accessable);
        return rcResult;
    }

    public void recheck(RCThreshold threshold) {
        this.lastThreshold = threshold;
        this.recheck();
    }

    public void recheck() {
        // 仅仅当前未被风控的需要进行再次检查
        if(this.accessable) {
            touch(this.lastThreshold, true);
        }
    }

    // 启动ban时间逻辑
    private void resetExpireTs(long currentTs, long banInterval) {
        this.touchTs = currentTs;
        this.expiredTs = this.touchTs + banInterval;
    }

    public String getBaseKey() {
        return this.baseKey;
    }

    public long retrieveDeltaInc() {
        long retVal = deltaInc.get();

        deltaInc.addAndGet(-retVal);
        return retVal;
    }

    public boolean needResetTouchTs() {
        return touchTsResetFlag;
    }

    public void resetTouchTs(long touchTs) {
        // 统一过期时间
        long expireDelta = this.expiredTs - this.touchTs;
        this.touchTs = touchTs;
        this.expiredTs = this.touchTs + expireDelta;
        this.touchTsResetFlag = false;
    }

    public long getTouchTs() {
        return this.touchTs;
    }

    public long getExpireTs() {
        return this.expiredTs;
    }

    /**
     * 若粘滞风控未触发，返回粘滞初始创建时间 + 默认粘滞关注时间
     * 否则返回粘滞风控起始时间 + 粘滞风控惩罚时间
     */
    public long getStickKeepEndTime() {
        return stickKeepEndTime;
    }

    public Set<String> getStickTimeSets() {
        return stickTimeSets;
    }

    /**
     * 更新touchTimes
     *
     * @param touchTimes
     */
    public void touchTimes(Long touchTimes) {
        this.touchTimes = touchTimes;
    }

    public void resetStickTimeSet(Set<String> setInCache) {
        this.stickTimeSets = new HashSet<String>();
        if(setInCache != null && setInCache.size() > 0) {
            stickTimeSets.addAll(setInCache);
        }
    }

    public void setLastThreshold(RCThreshold threshold) {
        this.lastThreshold = threshold;
    }

    @Override
    public String toString() {
        return "RCData ["  + uuid + "] [touchTs=" + new Date(touchTs * 1000) + ", touchTsResetFlag=" + touchTsResetFlag + ", touchTimes=" + touchTimes
                + ", stickKeepEndTime=" + stickKeepEndTime + ", accessable=" + accessable + ", deltaInc=" + deltaInc
                + ", expiredTs=" + new Date(expiredTs * 1000) + ", baseKey=" + baseKey + ", stickTimeSets=" + stickTimeSets
                + ", lastThreshold=" + lastThreshold + ", updateStickFlag=" + updateStickFlag
                + ", expireTimeRefreshFlag=" + expireTimeRefreshFlag + "]";
    }
}
