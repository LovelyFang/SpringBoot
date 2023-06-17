package com.tsxy;

import com.alibaba.fastjson.JSONObject;
import com.tsxy.entity.BaseMpVo;
import com.tsxy.methods.LockMethod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Liu_df
 * @Date 2023/4/23 11:18
 */
public class LockDemoTest {

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 60L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Test
    public void testLock() throws Exception {
        LockMethod lockMethod = new LockMethod();
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger j = new AtomicInteger(0);
        long start = System.nanoTime();
        while (i.getAndIncrement() <= 30) {
            int finalI = i.get();
            executorService.submit(() -> {
                try {
                    String s = lockMethod.drugsUpdate(finalI + "");
                    System.out.println(j.getAndIncrement() + "-----------------" + s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (j.get() == 31){
                    System.out.println("耗时" + (System.nanoTime() - start) / 1000000000);
                }
            });
        }
        TimeUnit.MINUTES.sleep(1);
    }

    @Test
    public void testLock1() throws Exception {
        LocalDate parse = LocalDate.parse("2023-05-11");
        System.out.println(parse.plus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ISO_DATE).toString());

        System.out.println(LocalDate.now().toString());
        System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

}
