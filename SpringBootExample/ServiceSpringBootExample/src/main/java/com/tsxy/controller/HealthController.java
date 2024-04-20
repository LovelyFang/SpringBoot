package com.tsxy.controller;

import com.alibaba.fastjson.JSON;
import com.tsxy.vo.BaseResultVo;
import com.tsxy.service.CustomService;
import com.tsxy.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liu_df
 * @Date 2022/11/15 0:08
 */

@RestController()
@RequestMapping("/health")
public class HealthController extends BaseController implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Resource
    private CustomService customService;

    private ThreadPoolTaskExecutor executorService = null;

    @RequestMapping("/index")
    public String index() {
        return "hello world";
    }

    @RequestMapping("/getProperty/{key}")
    public String getProperty(HttpServletRequest request, @PathVariable String key) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String hcUrl = scheme + "://" + serverName + ":" + serverPort;
        System.out.println(hcUrl);
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


    @RequestMapping("/cacheAspect")
    public void cacheAspect(HttpServletRequest request, HttpServletResponse response) {

//        Map<String, String[]> parameterMap = request.getParameterMap();   // {"key":["k1","k2"],"value":["v1"]}

        Map<String, String> params = getParameters(request);            // {"value":"k1","key":"v1"}
        BaseResultVo result = customService.aspectJMethod(params);
        this.sendSuccessData(response, result);

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
