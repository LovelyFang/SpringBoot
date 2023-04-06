package com.tsxy.completablefuture.utils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liu_df
 * @Date 2023/4/3 19:52
 */
public class FutureUtil {

    private static final Random RANDOM = new Random();

    // 人为睡眠一秒钟, 模拟网络延迟
    public static void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void randomDelay() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000 + RANDOM.nextInt(4000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
