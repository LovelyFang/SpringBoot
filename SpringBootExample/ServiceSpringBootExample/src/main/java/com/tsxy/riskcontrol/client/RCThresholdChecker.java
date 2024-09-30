package com.tsxy.riskcontrol.client;

import com.tsxy.riskcontrol.client.model.RCResult;
import com.tsxy.riskcontrol.client.model.RCThreshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;

/**
 * 对指定的阈值定义进行数据的状态检查
 * @Author Liu_df
 * @Date 2024/9/29 14:41
 */
@Component
public class RCThresholdChecker {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RCDataCache rcDataCache;

    public RCThresholdChecker() {
        // empty
    }

    public RCThresholdChecker(RCDataCache rcDataCache) {
        this.rcDataCache = rcDataCache;
    }

    public RCResult checkAccessAble(RCThreshold threshold, String target) {
        if(threshold.inWhitelist(target)) {
            return RCResult.PASS_RESULT;
        } else if(threshold.inBlacklist(target)) {
            logger.info(target + "为黑名单,直接风控");
            return RCResult.BAN_RESULT;
        }
        return rcDataCache.touchRCData(threshold, target);
    }

    public void ban(RCThreshold threshold, String target, long banEndTs) {
        logger.info("风控执行时间:{},目标用户:{},当次风控Type:{},风控阈值:{},风控持续时间:{}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()),target,threshold.getType(),threshold.getThreshold(),threshold.getInterval());
        rcDataCache.ban(threshold, target, banEndTs);
    }

    public void ensureEnv() {
        rcDataCache.ensureEnv();
    }
}
