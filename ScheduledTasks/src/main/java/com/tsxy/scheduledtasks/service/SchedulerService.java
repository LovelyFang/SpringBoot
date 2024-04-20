package com.tsxy.scheduledtasks.service;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.util.List;

/**
 * @Author Liu_df
 * @Date 2024/4/17 11:36
 */
public interface SchedulerService {


    /**
     * 重启所有任务
     */
    void start();

    /**
     * 重启所有任务
     */
    boolean isStarted();

    /**
     * 暂停所有任务
     */
    void paused();

    /**
     * 取得所有调度 Triggers
     */
    List<Trigger> getTriggers();

    /**
     * 获取 trigger
     */
    Trigger getTrigger(TriggerKey key);

    /**
     * 根据名称和组别暂停 Trigger
     */
    void pauseTrigger(TriggerKey key);

    /**
     * 恢复Trigger
     */
    void resumeTrigger(TriggerKey key);

    /**
     * 删除Trigger
     */
    void removeTrigger(TriggerKey key);

    /**
     * 执行
     */
    void runTrigger(TriggerKey key);

    /**
     * 获取调度器状态
     */
    Trigger.TriggerState getTriggerState(TriggerKey key);

    /**
     * 获取当前调度器
     */
    Scheduler getScheduler();

    boolean isMaster();


}
