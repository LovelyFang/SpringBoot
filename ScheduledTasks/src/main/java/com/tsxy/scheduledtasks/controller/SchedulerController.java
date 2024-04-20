package com.tsxy.scheduledtasks.controller;

import com.tsxy.scheduledtasks.entiey.ScheduleJob;
import com.tsxy.scheduledtasks.service.SchedulerService;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author Liu_df
 * @Date 2024/4/17 11:50
 */
@Controller
@RequestMapping("/scheduler")
public class SchedulerController {

    private final static Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    private final static EnumMap<Trigger.TriggerState, String> STATE_MAP = new EnumMap(Trigger.TriggerState.class);
    static {
        STATE_MAP.put(Trigger.TriggerState.NORMAL, "正常");// 0
        STATE_MAP.put(Trigger.TriggerState.PAUSED, "暂停");
        STATE_MAP.put(Trigger.TriggerState.COMPLETE, "完成");
        STATE_MAP.put(Trigger.TriggerState.ERROR, "异常");
        STATE_MAP.put(Trigger.TriggerState.BLOCKED, "正在执行");// 4
        STATE_MAP.put(Trigger.TriggerState.NONE, "不存在");// -1
    }

    @Resource
    private SchedulerService schedulerService;


    @RequestMapping("/index")
    public String scheduler(ModelMap model, HttpServletRequest request) {

        List<Trigger> triggers = schedulerService.getTriggers();
        List<ScheduleJob> list = new ArrayList<>();
        for (Trigger trigger : triggers) {
            ScheduleJob job = new ScheduleJob();
            TriggerKey key = trigger.getKey();
            job.setTriggerName(key.getName());
            job.setTriggerGroup(key.getGroup());
            Trigger.TriggerState state = schedulerService.getTriggerState(key);
            job.setState(state.name());
            job.setStateName(STATE_MAP.get(state));
            job.setEndTime(trigger.getEndTime());
            job.setNextFireTime(trigger.getNextFireTime());
            job.setStartTime(trigger.getStartTime());
            job.setPreviousFireTime(trigger.getPreviousFireTime());
            if (trigger instanceof CronTrigger) {
                job.setCronExpression(((CronTrigger) trigger).getCronExpression());
            }
            list.add(job);

        }
        model.put("triggers", list);
        model.put("serverIp", request.getLocalAddr());
        model.put("port", request.getLocalPort());
        model.put("master", schedulerService.isMaster());
        model.put("start", schedulerService.isStarted());

        return "/scheduler/index";
    }

    @RequestMapping("/start")
    @ResponseBody
    public Map<String, Object> start(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            logger.debug("定时任务start手动操作");
            schedulerService.start();
            map.put("status", true);
        } catch (Exception e) {
            logger.error("启动所有定时任务出错", e);
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;

    }

    @RequestMapping("/stop")
    @ResponseBody
    public Map<String, Object> stop(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            logger.debug("定时任务stop手动操作");
            schedulerService.paused();
            map.put("status", true);
        } catch (Exception e) {
            logger.error("启动所有定时任务出错", e);
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/pause")
    @ResponseBody
    public Map<String, Object> pauseTrigger(HttpServletRequest request, String triggerName, String group) {
        Map<String, Object> map = new HashMap<>();

        if (!schedulerService.isStarted()) {
            map.put("status", false);
            map.put("message", "任务未启动");
            return map;
        }

        try {
            logger.debug("定时任务pause手动操作,triggerName:{},group:{}", triggerName, group);
            schedulerService.pauseTrigger(new TriggerKey(triggerName, group));
            map.put("status", true);
        } catch (Exception e) {
            logger.error("暂停所有定时任务出错", e);
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/resume")
    @ResponseBody
    public Map<String, Object> resumeTrigger(HttpServletRequest request, String triggerName, String group) {
        Map<String, Object> map = new HashMap<>();

        if (!schedulerService.isStarted()) {
            map.put("status", false);
            map.put("message", "任务未启动");
            return map;
        }

        try {
            logger.debug("定时任务resume手动操作,triggerName:{},group:{}", triggerName, group);
            schedulerService.resumeTrigger(new TriggerKey(triggerName, group));
            map.put("status", true);
        } catch (Exception e) {
            logger.error("重启所有定时任务出错", e);
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("/run")
    @ResponseBody
    public Map<String, Object> runTrigger(HttpServletRequest request, String triggerName, String group) {
        Map<String, Object> map = new HashMap<>();

        if (!schedulerService.isStarted()) {
            map.put("status", false);
            map.put("message", "任务未启动");
            return map;
        }

        try {
            logger.debug("定时任务run手动操作,triggerName:{},group:{}", triggerName, group);
            schedulerService.runTrigger(new TriggerKey(triggerName, group));
            map.put("status", true);
        } catch (Exception e) {
            logger.error("执行定时任务出错", e);
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

}
