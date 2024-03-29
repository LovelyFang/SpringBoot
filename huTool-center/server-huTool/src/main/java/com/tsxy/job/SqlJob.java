package com.tsxy.job;

import com.alibaba.fastjson.JSON;
import com.tsxy.dao.CronMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @Author Liu_df
 * @Date 2023/5/12 16:06
 */
@Component
@EnableScheduling
public class SqlJob implements SchedulingConfigurer {

    // 创建日志对象
    final Logger logger = Logger.getLogger(SqlJob.class);

    @Autowired
    protected CronMapper cronMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(this::process,
                triggerContext -> {
                    String cron = "0/10 * * * * ?";
//                    String cron = cronMapper.getCron(1);
                    if (cron.isEmpty()) {
                        logger.error("cron is null");
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }

    private void process() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.info(JSON.toJSON(stackTrace));
//        logger.info("基于接口的定时任务");
    }

}
