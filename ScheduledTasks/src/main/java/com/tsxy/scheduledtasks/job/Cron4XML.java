package com.tsxy.scheduledtasks.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * @Author Liu_df
 * @Date 2022/11/22 0:47
 */
public class Cron4XML {
    Logger logger = LoggerFactory.getLogger(Cron4XML.class);

    public void configurationTasks(){
        logger.info("*****开始执行定时任务*****");
        System.out.println("基于XML的：" + LocalDateTime.now());
        logger.info("*****结束执行定时任务*****");
    }
}
