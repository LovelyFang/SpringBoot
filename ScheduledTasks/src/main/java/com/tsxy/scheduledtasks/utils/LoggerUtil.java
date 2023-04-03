package com.tsxy.scheduledtasks.utils;

import com.gzhc365.component.utils.entity.HcContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    /**
     * 根据参数获取不同日志名
     *
     * @param hcContext
     * @return
     */
    public static Logger getLogger(HcContext hcContext, final Logger logger) {
        if (hcContext != null && StringUtils.isNotBlank(hcContext.getAttr("hisTradeLogName"))) {
            String loggerName = hcContext.getAttr("hisTradeLogName");
            return LoggerFactory.getLogger(loggerName);
        }
        return logger;
    }
}
