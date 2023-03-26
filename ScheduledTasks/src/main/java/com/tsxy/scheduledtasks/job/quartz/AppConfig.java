package com.tsxy.scheduledtasks.job.quartz;

/**
 * @Author Liu_df
 * @Date 2022/11/22 22:33
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统的配置项-加载config数据
 *
 * @version 1.0
 * @since JDK 1.7
 */
public class AppConfig {

    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private static Map<String, Map<String, String>> config = new ConcurrentHashMap<String, Map<String, String>>();

    public final static String DEFAULT_GROUP = "default";

    public static Map<String, String> getGroup(String group) {
        Map<String, String> map = config.get(group);
        if (map == null) {
            return new ConcurrentHashMap<>();
        }
        return map;
    }

    public static String getString(String name, String defaultValue) {
        String value = getGroup(DEFAULT_GROUP).get(name);
        if (value == null) {
            logger.debug("系统配置项{}值为空", name);
            return defaultValue;
        }
        return value;
    }

    public static int getInt(String name, int defaultValue) {
        String value = getGroup(DEFAULT_GROUP).get(name);
        if (StringUtils.isBlank(value)) {
            logger.debug("系统配置项{}值为空", name);
            return defaultValue;
        }
        return Integer.valueOf(value);
    }

    public static long getLong(String name, long defaultValue) {
        String value = getGroup(DEFAULT_GROUP).get(name);
        if (StringUtils.isBlank(value)) {
            logger.debug("系统配置项{}值为空", name);
            return defaultValue;
        }
        return Long.valueOf(value);
    }

    public static long getLong(String name, String secondName, long defaultValue) {
        String value = getGroup(DEFAULT_GROUP).get(name);
        if (StringUtils.isBlank(value)) {
            String secondValue = getGroup(DEFAULT_GROUP).get(secondName);
            if (StringUtils.isBlank(secondValue)) {
                logger.debug("系统配置项{}值为空", name);
                return defaultValue;
            }
            return Long.valueOf(secondValue);
        }
        return Long.valueOf(value);
    }

    public static double getDouble(String name, double defaultValue) {
        String value = getGroup(DEFAULT_GROUP).get(name);
        if (StringUtils.isBlank(value)) {
            logger.debug("系统配置项{}值为空", name);
            return defaultValue;
        }
        return Double.valueOf(value);
    }

    public static synchronized void init(Map<String, Map<String, String>> config) {
        AppConfig.config = new ConcurrentHashMap<>(config);
    }
}
