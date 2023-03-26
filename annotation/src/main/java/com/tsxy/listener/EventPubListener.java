package com.tsxy.listener;

import com.tsxy.entity.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:32
 */
@Component
public class EventPubListener {

    @Autowired
    private ApplicationContext applicationContext;

    // 事件发布方法
    public void pushListener(SysLog sysLogEvent) {
        applicationContext.publishEvent(sysLogEvent);
    }
}
