package com.tsxy.methods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Liu_df
 * @Date 2023/4/23 11:19
 */
public class LockMethod {

    private static final JSONArray westernJSONArr = new JSONArray();

    ReentrantLock lock = new ReentrantLock();

    public String drugsUpdate(String str) throws Exception {

        try {
            lock.lock();
            handleHisDrugInfo(str);
            if (westernJSONArr.size() > 10){
                requestHCUpdateDrugs();
            }
        } finally {
            lock.unlock();
        }
        return "success" + str;
    }

    private void handleHisDrugInfo(String seq) throws Exception {
        TimeUnit.SECONDS.sleep(1);
        westernJSONArr.add(seq);
    }

    private void requestHCUpdateDrugs() throws Exception {
        System.out.println("模拟发送请求\t\t\t" + JSON.toJSON(westernJSONArr));
        TimeUnit.SECONDS.sleep(4);
        System.out.println("模拟发送请求-睡完觉后\t" + JSON.toJSON(westernJSONArr));
        westernJSONArr.clear();
    }


}
