package com.tsxy.riskcontrol.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 秒级时间戳优化类
 * @Author Liu_df
 * @Date 2024/9/29 14:27
 */
public class SecTimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecTimeUtil.class);

    static {
        new Thread(new Runnable() {

            public void run() {
                long delta = 1000;

                while(true) {
                    try {
                        Thread.sleep(delta);
                    } catch (InterruptedException e) {
                        logger.warn("time thread be interupted, break the loop.", e);
                        Thread.currentThread().interrupt();
                        break;
                    }

                    long ts = System.currentTimeMillis();
                    calcCurrentSec(ts);
                    delta = 1000 - (ts % 1000);
                }
            }
        }).start();
    }

    private static long currentSec = calcCurrentSec(System.currentTimeMillis());

    public static long currentSec() {
        return currentSec;
    }

    private static long calcCurrentSec(long ts) {
        return currentSec = ts / 1000;
    }
}
