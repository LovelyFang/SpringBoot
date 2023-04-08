package com.tsxy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsxy.utils.PropertyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liu_df
 * @Date 2022/11/15 0:08
 */

@RestController
public class Health implements InitializingBean {

    private ThreadPoolTaskExecutor executorService = null;

    @RequestMapping("/health")
    public String health() {
        return "hello world";
    }

    @RequestMapping("/getProperty/{key}")
    public String getProperty(@PathVariable String key) {
        String property = PropertyUtil.getProperty(key);
        return property;
    }

    @RequestMapping("/getCountDownLatch")
    public String getCountDownLatch() throws Exception {
        CountDownLatch latch = new CountDownLatch(4);
        List<String> result = new ArrayList<>();
        System.out.println(LocalTime.now() + "  开始执行");
        executorService.execute(() -> {
            try {
                result.add("abc");
                System.out.println(LocalTime.now() + " 增加abc");
            } catch (Exception e){
                System.out.println("查询处方订单异常");
            } finally {
                latch.countDown();
            }
        });
        System.out.println(LocalTime.now() + " ==> " + JSON.toJSONString(result));
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                result.add("123");
                System.out.println(LocalTime.now() + " 增加123");
            } catch (Exception e){
                System.out.println("查询处方订单异常");
            } finally {
                latch.countDown();
            }
        });
        System.out.println(LocalTime.now() + " ==> " + JSON.toJSONString(result));
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                result.add("abc123");
                System.out.println(LocalTime.now() + " 增加abc123");
            } catch (Exception e){
                System.out.println("查询处方订单异常");
            } finally {
                latch.countDown();
            }
        });
        TimeUnit.SECONDS.sleep(1);
        System.out.println(LocalTime.now() + " ==> " + JSON.toJSONString(result));
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                result.add("123abc");
                System.out.println(LocalTime.now() + " 增加123abc");
            } catch (Exception e){
                System.out.println("查询处方订单异常");
            } finally {
                latch.countDown();
            }
        });
        System.out.println(LocalTime.now() + " ==> " + JSON.toJSONString(result));
        latch.await();
        System.out.println(LocalTime.now() + "  处理完成");
        return JSON.toJSONString(result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executorService = new ThreadPoolTaskExecutor();
        this.executorService.setCorePoolSize(20);
        this.executorService.setMaxPoolSize(1000);
        this.executorService.setThreadNamePrefix("queryOrder-");
        this.executorService.setQueueCapacity(800);
        this.executorService.setKeepAliveSeconds(120);
        this.executorService.setAllowCoreThreadTimeOut(true);
        this.executorService.initialize();
    }
}
