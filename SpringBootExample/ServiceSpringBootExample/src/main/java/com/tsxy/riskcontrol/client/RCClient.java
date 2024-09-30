package com.tsxy.riskcontrol.client;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.tsxy.riskcontrol.client.model.RCResult;
import com.tsxy.riskcontrol.client.model.RCStatus;
import com.tsxy.riskcontrol.client.model.RCThreshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:28
 */
@Component
public class RCClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RCConfig rcConfig;

    @Autowired
    private RCThresholdChecker rcThresholdChecker;

    @Value("${rc.max.allowed.delay.ms:5000}")
    private int maxAllowedDelayMs = 5000;

    @Value("${rc.default.module:}")
    private String module;

    @Value("${rc.switch.accessable:true}")
    private String rcAccessAble;

    private static String DEFAULT_ACCESS_ABLE = "false";

    public RCClient() {
        // empty
    }

    public RCClient(RCConfig rcConfig, RCThresholdChecker rcThresholdChecker, String defaultModule, int maxAllowedDelayMs) {
        this.rcConfig = rcConfig;
        this.rcThresholdChecker = rcThresholdChecker;
        this.module = defaultModule;
        this.maxAllowedDelayMs = maxAllowedDelayMs;
    }

    /**
     * 判断是否被风控 (触发时间为当前时间、因子之间默认相互影响)
     * @param identifier
     * @param targetMap
     */
    public boolean accessAble(String identifier, Map<String, String> targetMap) {
        return accessAble(identifier, false, targetMap);
    }

    public RCStatus status(String identifier) {
        return status(this.module, identifier);
    }

    public RCStatus status(String module, String identifier) {
        return rcConfig.getStatus(module, identifier);
    }

    /**
     * 判断是否被风控(触发时间为当前时间)
     *
     * @param identifier 风控标示 (归属业务模块)
     * @param inDependency 因素是否相互影响
     * @param targetMap 判定风控因素的键值对
     */
    public boolean accessAble(String identifier, boolean inDependency, Map<String, String> targetMap) {
        return accessAble(identifier, System.currentTimeMillis(), inDependency, targetMap);
    }

    public boolean accessAble(String identifier, long timestamp, boolean inDependency, Map<String, String> targetMap) {
        return accessAble(this.module, identifier, timestamp, inDependency, targetMap);
    }

    /**
     * 判断是否被风控
     *
     * @param module 业务模块
     * @param identifier 风控标示 (归属业务模块)
     * @param timestamp 业务发生的时间戳
     * @param inDependency 因素是否相互影响
     * @param targetMap 判定风控因素的键值对
     * @return true 如果未被风控，否则为false
     */
    public boolean accessAble(String module, String identifier, long timestamp, boolean inDependency, Map<String, String> targetMap) {
        if(null== rcAccessAble || DEFAULT_ACCESS_ABLE.equals(rcAccessAble)){
            logger.info("{}已关闭风控", identifier);
            return true;
        }

        RCResult rcResult = RCResult.DEFAULT_PASS_RESULT;

        // 业务时间若超时访问 不进入风控体系
        if(System.currentTimeMillis() - timestamp < maxAllowedDelayMs) {

            try {
                rcThresholdChecker.ensureEnv();

                RCResult tmpRCResult;
                for(Entry<String, String> keyval : targetMap.entrySet()) {
                    tmpRCResult = accessAble(module, identifier, keyval.getKey(), keyval.getValue());

                    // 白名单，直接跳出
                    if(tmpRCResult.ignoreMore()) {
                        rcResult = tmpRCResult;
                        break;
                    } else if(!tmpRCResult.isAccessAble()) {
                        rcResult = tmpRCResult;
                    }
                }

                // 若触发风控且相互关联，进行关联禁用
                if(!rcResult.isAccessAble() && !inDependency && rcResult.getBanEndTs() > 0) {
                    for(Entry<String, String> keyval : targetMap.entrySet()) {
                        ban(module, identifier, keyval.getKey(), keyval.getValue(), rcResult.getBanEndTs());
                    }
                }
            } catch(Exception e) {
                logger.warn("error while rc check: " + e.getMessage(), e);
            }
        }

        return rcResult.isAccessAble();
    }

    private RCResult accessAble(String module, String identifier, String key, String value) {

        List<RCThreshold> thresholds = rcConfig.thresholds(module, identifier, key);

        RCResult rcResult = RCResult.DEFAULT_PASS_RESULT;
        if(thresholds != null && thresholds.size() > 0) {
            for(RCThreshold threshold : thresholds) {
                // 任意触发风控即不可访问，但所有阈值检测仍需执行
                rcResult = rcThresholdChecker.checkAccessAble(threshold, value);

                if(rcResult.ignoreMore()) {
                    break;
                }
            }
        }

        return rcResult;
    }

    private void ban(String module, String identifier, String key, String value, long banEndTs) {
        List<RCThreshold> thresholds = rcConfig.thresholds(module, identifier, key);

        if(thresholds != null && thresholds.size() > 0) {
            for(RCThreshold threshold : thresholds) {
                // 最低成本的禁用当下访问
                rcThresholdChecker.ban(threshold, value, banEndTs);
            }
        }
    }
}

