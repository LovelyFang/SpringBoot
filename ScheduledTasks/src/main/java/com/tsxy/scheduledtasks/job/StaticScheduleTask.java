package com.tsxy.scheduledtasks.job;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @Author Liu_df
 * @Date 2022/11/21 23:44
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {

    @Scheduled(cron = "0/5 * * * * ?")
    public void configurationTasks(){
        System.out.println("执行定时任务,基于注解的：" + LocalDateTime.now());
    }

}
