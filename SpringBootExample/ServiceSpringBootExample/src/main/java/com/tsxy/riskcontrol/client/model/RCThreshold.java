package com.tsxy.riskcontrol.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 每个阈值
 * @Author Liu_df
 * @Date 2024/9/29 14:26
 */

public class RCThreshold implements Serializable {

    private static final long serialVersionUID = -6998561442769965215L;

    /**
     * 存储在配置中
     * {module}:{identifier}:{key}
     */
    private String type;

    /**
     *  单次触发的时间窗口
     */
    private int interval;

    /**
     *  单次触发防控的阈值
     */
    private int threshold;

    /**
     *  单次触发防控后的防控周期，若不指定，等于interval
     */
    private int banInterval;

    /**
     *  连续触发防控的时间窗口
     */
    private int stickInterval;

    /**
     *  连续触发防控的阈值
     */
    private int stickThreshold;

    /**
     *  连续触发防控后的防控周期，若不指定，等于stickInterval
     */
    private int stickBanInterval;

    /**
     * 黑名单
     */
    private Set<String> blacklist;

    /**
     * 黑名单通配
     */
    private List<Pattern> blRegexList;

    /**
     * 白名单
     */
    private Set<String> whitelist;

    /**
     * 白名单通配
     */
    private List<Pattern> wlRegexList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getBanInterval() {
        return banInterval < 1 ? interval : banInterval;
    }

    public void setBanInterval(int banInterval) {
        this.banInterval = banInterval;
    }

    public int getStickInterval() {
        return stickInterval;
    }

    public void setStickInterval(int stickInterval) {
        this.stickInterval = stickInterval;
    }

    public int getStickThreshold() {
        return stickThreshold;
    }

    public void setStickThreshold(int stickThreshold) {
        this.stickThreshold = stickThreshold;
    }

    public int getStickBanInterval() {
        return stickBanInterval < 1 ? stickInterval : stickBanInterval;
    }

    public void setStickBanInterval(int stickBanInterval) {
        this.stickBanInterval = stickBanInterval;
    }

    public Set<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Set<String> blacklist) {
        this.blacklist = blacklist;
    }

    public Set<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<String> whitelist) {
        this.whitelist = whitelist;
    }

    public boolean assureConfig(String key) {

        if(type.startsWith(key)) {
            parseRegex();

            return true;
        }

        return false;
    }

    private static final String REGEX_PREFIX = "re.";
    public static final int REGEX_LEN = REGEX_PREFIX.length();

    private void parseRegex() {
        if(whitelist != null) {
            wlRegexList = new ArrayList<>();

            List<String> rmList = new ArrayList<>();
            for(String wl : whitelist) {
                if(wl.startsWith(REGEX_PREFIX)) {
                    wlRegexList.add(Pattern.compile(wl.substring(REGEX_LEN)));
                    rmList.add(wl);
                }
            }

            whitelist.removeAll(rmList);
        }

        if(blacklist != null) {
            blRegexList = new ArrayList<>();

            List<String> rmList = new ArrayList<>();
            for(String bl : blacklist) {
                if(bl.startsWith(REGEX_PREFIX)) {
                    blRegexList.add(Pattern.compile(bl.substring(REGEX_LEN)));
                    rmList.add(bl);
                }
            }

            blacklist.removeAll(rmList);
        }
    }

    public boolean inWhitelist(String target) {
        boolean flag = whitelist != null && whitelist.contains(target);
        if(!flag && wlRegexList != null) {
            for(Pattern regex : wlRegexList) {
                if(flag = regex.matcher(target).find()) {
                    break;
                }
            }
        }

        return flag;
    }

    public boolean inBlacklist(String target) {
        boolean flag = blacklist != null && blacklist.contains(target);
        if(!flag && blRegexList != null) {
            for(Pattern regex : blRegexList) {
                if(flag = regex.matcher(target).find()) {
                    break;
                }
            }
        }

        return flag;
    }

    @Override
    public String toString() {
        return "RCThreshold [type=" + type + ", interval=" + interval + ", threshold=" + threshold + ", banInterval="
                + banInterval + ", stickInterval=" + stickInterval + ", stickThreshold=" + stickThreshold
                + ", stickBanInterval=" + stickBanInterval + ", blacklist=" + blacklist + ", blRegexList=" + blRegexList
                + ", whitelist=" + whitelist + ", wlRegexList=" + wlRegexList + "]";
    }
}
