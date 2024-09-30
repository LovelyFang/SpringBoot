package com.tsxy.riskcontrol.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.tsxy.riskcontrol.client.model.RCData;
import com.tsxy.riskcontrol.client.model.RCResult;
import com.tsxy.riskcontrol.client.model.RCThreshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:40
 */

@Component
public class RCDataCache {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${rc.data.cache.async:true}")
    private boolean async = true;

    @Value("${rc.data.cache.sync.interval.ms:1000}")
    private int asyncInterval = 1000;
    @Value("${rc.switch.accessable:true}")
    private String rcAccessable;
    private @Autowired RCCache rcCache;

    private Map<String, RCData> cacheMap = new ConcurrentHashMap<>();

    private volatile boolean runningFlag;

    public RCDataCache() {
        // empty
    }

    public RCDataCache(RCCache rcCache, boolean async, int asyncInterval) {
        this.rcCache = rcCache;
        this.async = async;
        this.asyncInterval = asyncInterval;
        init();
    }

    private ThreadLocal<Map<String, RCData>> rcDataCacheTL = new ThreadLocal<Map<String,RCData>>(){
        @Override
        protected Map<String, RCData> initialValue() {
            return new HashMap<String, RCData>();
        }
    };

    @PostConstruct
    public synchronized void init() {
        if(rcAccessable==null || "false".equals(rcAccessable)){
            return;
        }
        if(async && !runningFlag) {
            runningFlag = true;
            new Thread(new Runnable() {
                public void run() {
                    while(runningFlag) {

                        try {
                            syncAndClear();
                        } catch (Exception e) {
                            logger.warn("error while sync and clear, ex: {}", e);
                        }

                        try {
                            Thread.sleep(asyncInterval);
                        } catch (InterruptedException e) {
                            logger.warn("the thread[" + Thread.currentThread().getName() + "] be interrupted, exit the loop.", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                }}, "rc-datacache-sync-thread").start();
        }
    }

    public void ensureEnv() {
        if(!async) {
            rcDataCacheTL.get().clear();
        }
    }

    /**
     * 针对每个threshold - target 均有两个cache keyval pair
     * single touch key
     * 当次触发的 key
     * stick touch key
     * @param threshold
     * @param target
     * @return
     */
    public RCResult touchRCData(RCThreshold threshold, String target) {

        RCResult rcResult = null;
        RCData rcData = null;
        String cacheKey = threshold.getType() + ":" + target;

        if(async) {
            rcData = cacheMap.get(cacheKey);

            if(rcData == null || !rcData.isValid()) {
                rcData = loadRCData(cacheKey, threshold.getInterval());
                cacheMap.put(cacheKey, rcData);
            }
            rcResult = rcData.touch(threshold);
        } else {
            rcData = loadRCData(cacheKey, threshold.getInterval());

            rcData.recheck(threshold);
            rcResult = rcData.touch(threshold);

            sync(rcData);
        }

        return rcResult;
    }

    public void ban(RCThreshold threshold, String target, long banEndTs) {
        RCData rcData = null;

        String cacheKey = threshold.getType() + ":" + target;

        if(async) {
            rcData = cacheMap.get(cacheKey);

            if(rcData.isValid()) {
                rcData.ban(banEndTs);
                return;
            }
        }

        rcData = loadRCData(cacheKey, threshold.getInterval());
        rcData.ban(banEndTs);
        sync(rcData);
    }

    private RCData loadRCData(String cacheSource, final long expireTime) {
        if(async) {
            return new RCData(rcCache.baseKey(cacheSource), expireTime);
        }

        return rcDataCacheTL.get().computeIfAbsent(cacheSource, new Function<String, RCData>() {
            @Override
            public RCData apply(String cacheSource) {
                return rcCache.getWithTimeMark(cacheSource, expireTime);
            }
        });
    }

    /**
     * 同步风控记录数据到数据库
     * @param rcData
     */
    private void sync(final RCData rcData) {

        if(rcData.needResetTouchTs()) {
            long touchTs = rcCache.ensureTouchTs(rcData.getBaseKey(), rcData.getTouchTs(), rcData.getExpireTs());
            if(touchTs > 0) {
                rcData.resetTouchTs(touchTs);
            }
        }

        long deltaInc = rcData.retrieveDeltaInc();

        if(deltaInc > 0) {
            rcCache.inc(rcData.getBaseKey(), deltaInc, new Consumer<Long>() {
                @Override
                public void accept(Long t) {
                    rcData.touchTimes(t);
                }
            });
            // disable refresh in everytime ###
            if(!rcData.accessable()) {
                rcCache.resetExpireTime(rcData.getBaseKey(), rcData.getExpireTs());
            }

            // 是否需要更新粘滞数据
            if(rcData.needUpdateStick()) {
                rcCache.refreshStickTimes(rcData.getBaseKey(), rcData.getStickKeepEndTime(), rcData.getStickTimeSets());
            }
        } else if(rcData.needRefreshExpireTime()) {
            rcCache.resetExpireTime(rcData.getBaseKey(), rcData.getExpireTs());
        }

        // 若当前节点已经被风控，无须继续同步
        if(rcData.accessable()) {
            rcCache.loadAndRefresh(rcData);
        }
    }

    private void syncAndClear() {
        List<String> keys = new ArrayList<String>(cacheMap.keySet());

        rcCache.startPipe();
        try {
            for(String key : keys) {
                RCData rcData = cacheMap.get(key);

                if(rcData != null) {
                    if(rcData.isValid()) {
                        sync(rcData);
                    } else {
                        cacheMap.remove(key);
                    }
                }
            }
        } finally {
            rcCache.endPipe();
        }
    }

    @PreDestroy
    public void destory() {
        this.runningFlag = false;
    }
}

