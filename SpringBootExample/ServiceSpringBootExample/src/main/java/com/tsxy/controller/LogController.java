package com.tsxy.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Liu_df
 * @Date 2024/4/22 10:50
 */
@Controller
@RequestMapping("/log")
public class LogController {

    public static final Logger logger = LoggerFactory.getLogger(LogController.class);

    // ================================================log4j===================================================================
    @RequestMapping(value = "/log4j/modify/root/{level}", method = RequestMethod.GET)
    @ResponseBody
    public String updateLog4jLevel(@PathVariable("level") String levelName) {
//        org.apache.log4j.Level level = Level.toLevel(levelName);
//        LogManager.getRootLogger().setLevel(level);
        return "修改log4j的root为{" + levelName + "}级别成功";
    }

    @RequestMapping(value = "/log4j/modify/package/{package}/{level}", method = RequestMethod.GET)
    @ResponseBody
    public String updateLog4jLevel(@PathVariable("package") String packageName,
                                   @PathVariable("level") String levelName) {
//        org.apache.log4j.Level level = org.apache.log4j.Level.toLevel(levelName);
//        LogManager.getLogger(packageName).setLevel(level);
        return "修改log4j的package={" + packageName + "}为{" + levelName + "}日志级别成功";
    }


    // ================================================Logback===================================================================
    @RequestMapping(value = "/logback/modify/root/{level}", method = RequestMethod.GET)
    @ResponseBody
    public String updateRootLogbackLevel(@PathVariable("level") String levelName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logbackLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        String levelStr = logbackLogger.getLevel().levelStr;
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.toLevel(levelName));
        return "修改logback的root为{" + levelName + "}级别成功,修改前为{" + levelStr + "}";
    }

    @RequestMapping(value = "/logback/modify/package/{package}/{level}", method = RequestMethod.GET)
    @ResponseBody
    public String updateLogbackLevel(@PathVariable("package") String packageName, @PathVariable("level") String levelName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(packageName).setLevel(Level.toLevel(levelName));
        return "修改logback的package={" + packageName + "}为{" + levelName + "}日志级别成功";
    }



}
