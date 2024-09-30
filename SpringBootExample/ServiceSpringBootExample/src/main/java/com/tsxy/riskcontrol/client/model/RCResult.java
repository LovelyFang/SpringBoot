package com.tsxy.riskcontrol.client.model;

/**
 * 风控检查结果对象
 * @Author Liu_df
 * @Date 2024/9/29 14:25
 */

public class RCResult {

    public static final RCResult DEFAULT_PASS_RESULT = new RCResult(true, false);
    public static final RCResult PASS_RESULT = new RCResult(true, true);
    public static final RCResult BAN_RESULT = new RCResult(false, true);

    private boolean accessAble = true;

    private boolean ignoreMore = false;

    private long banEndTs = 0;

    public RCResult() {
        // empty
    }

    private RCResult(boolean accessAble, boolean ignoreMore) {
        this.accessAble = accessAble;
        this.ignoreMore = ignoreMore;
    }

    public boolean isAccessAble() {
        return accessAble;
    }

    public void setAccessAble(boolean accessAble) {
        this.accessAble = accessAble;
    }

    public long getBanEndTs() {
        return banEndTs;
    }

    public void setBanEndTs(long banEndTs) {
        this.banEndTs = banEndTs;
    }

    public boolean ignoreMore() {
        return ignoreMore;
    }
}
