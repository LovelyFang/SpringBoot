package com.tsxy.listener;

import com.tsxy.entity.SysLog;
import com.tsxy.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:32
 */
@Slf4j
@Component
public class MyEventListener {

    @Autowired
    private TestService testService;

    // 开启异步
    @Async
    // 开启监听
    @EventListener(SysLog.class)
    public void saveSysLog(SysLog event) {
        log.info("=====即将异步保存到数据库======");
        testService.saveLog(event);
    }

}
