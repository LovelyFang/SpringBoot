package com.tsxy.completablefuture;

import com.tsxy.completablefuture.service.FutureMethod;
import com.tsxy.completablefuture.service.impl.AsyncFutureMethod;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @Author Liu_df
 * @Date 2023/4/3 20:07
 */
public class CompletableFutureTest {



    public static void main(String[] args) {

//        System.out.println(Runtime.getRuntime().availableProcessors());

        FutureMethod method = new AsyncFutureMethod();
        long startTime = System.nanoTime();
//        System.out.println(method.findPrices("myIPhone13ProMax"));
        long duration = (System.nanoTime() - startTime) / 1000000;
//        System.out.println("耗时：" + duration + "毫秒");

//        System.out.println("-------------------------下边的是有折扣的---------------------------");
//        startTime = System.nanoTime();
//        System.out.println(method.findDiscountPrice("myIPhone13ProMax"));
//        duration = (System.nanoTime() - startTime) / 1000000;
//        System.out.println("耗时：" + duration + "毫秒");

        System.out.println("-------------------------下边是有随机延迟有折扣的---------------------------");
        startTime = System.nanoTime();
        Stream<CompletableFuture<String>> myIPhone13ProMax = method.findDiscountPriceByRandomDelay("myIPhone13ProMax");

        long finalStartTime = startTime;
        CompletableFuture[] completableFutures = myIPhone13ProMax.map(f ->
                f.thenAccept(s ->
                        System.out.println(s + " ==> 完成, 耗时：" + (System.nanoTime() - finalStartTime) / 1000000 + "毫秒")))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();
        System.out.println("全部完成: 耗时：" + (System.nanoTime() - finalStartTime) / 1000000 + "毫秒");
    }


}
