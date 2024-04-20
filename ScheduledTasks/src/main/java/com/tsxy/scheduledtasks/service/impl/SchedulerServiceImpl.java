package com.tsxy.scheduledtasks.service.impl;

import com.tsxy.scheduledtasks.service.SchedulerService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author Liu_df
 * @Date 2024/4/17 11:37
 */
@Service
public class SchedulerServiceImpl implements SchedulerService, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Resource
    private Scheduler scheduler;

    public void start() {
        try {
            // 只有没启动的才重新启动
            if (!isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException ex) {
            logger.info("启动定时器出错", ex);
            throw new RuntimeException(ex);
        }
    }

    public boolean isStarted() {
        try {
            return !this.scheduler.isInStandbyMode();
        } catch (SchedulerException ex) {
            logger.info("判断定时器是否启动出错", ex);
            return false;
        }
    }

    public void paused() {
        try {
            if (this.scheduler.isStarted()) {
                this.scheduler.standby();
            }
        } catch (SchedulerException ex) {
            logger.info("停止定时器出错", ex);
            throw new RuntimeException(ex);
        }
    }

    public List<Trigger> getTriggers() {

        List<Trigger> list = new ArrayList<>();
        try {
            List<String> groups = scheduler.getTriggerGroupNames();
            if (groups == null) {
                return list;
            }
            for (String group : groups) {
                Set<TriggerKey> tiggTriggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(group));
                for (TriggerKey triggerKey : tiggTriggerKeys) {
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    list.add(trigger);
                }
            }
        } catch (SchedulerException e) {
            logger.error("获取调度任务列表失败", e);
        }
        return list;
    }

    public Trigger getTrigger(TriggerKey key) {
        try {
            return scheduler.getTrigger(key);
        } catch (SchedulerException e) {
            logger.error("获取调度任务失败", e);
        }
        return null;
    }

    public void pauseTrigger(TriggerKey key) {
        try {
            Trigger.TriggerState triggerState = getTriggerState(key);
            if (Trigger.TriggerState.PAUSED.equals(triggerState)) {
                logger.debug("trigger[{}]已暂停，无需再次暂停", key);
                return;
            }
            scheduler.pauseTrigger(key);
        } catch (SchedulerException e) {
            logger.error("暂停调度任务失败", e);
            throw new RuntimeException(e);
        }
    }

    public void resumeTrigger(TriggerKey key) {
        try {

            Trigger.TriggerState triggerState = getTriggerState(key);
            if (!Trigger.TriggerState.PAUSED.equals(triggerState)) {
                logger.debug("trigger[{}]已启动，无需再次启动", key);
                return;
            }

            setStartTime(key);
            scheduler.resumeTrigger(key);
        } catch (SchedulerException e) {
            logger.error("重启调度任务失败", e);
            throw new RuntimeException(e);
        }
    }

    public void removeTrigger(TriggerKey key) {
        try {
            scheduler.pauseTrigger(key);// 停止触发器
            scheduler.unscheduleJob(key);// 移除触发器
        } catch (SchedulerException e) {
            logger.error("移除调度任务失败", e);
            throw new RuntimeException(e);
        }
    }

    public void runTrigger(TriggerKey key) {
        try {
            Trigger trigger = scheduler.getTrigger(key);
            scheduler.triggerJob(trigger.getJobKey(), trigger.getJobDataMap());
        } catch (SchedulerException e) {
            logger.error("执行调度任务失败", e);
            throw new RuntimeException(e);
        }
    }

    public Trigger.TriggerState getTriggerState(TriggerKey key) {
        try {
            return scheduler.getTriggerState(key);
        } catch (SchedulerException e) {
            logger.error("获取trigger状态出错", e);
        }
        return Trigger.TriggerState.NORMAL;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    private void setStartTime(TriggerKey triggerKey) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(triggerKey);
        try {
            // 下一次执行时间在这之前
            if (trigger.getNextFireTime().before(new Date()) && trigger instanceof OperableTrigger) {
                ((OperableTrigger) trigger).setStartTime(new Date());
            }
        } catch (Exception e) {
            logger.warn("重新设置startTime出错", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isMaster() {
        // TODO Auto-generated method stub
        return false;
    }
}