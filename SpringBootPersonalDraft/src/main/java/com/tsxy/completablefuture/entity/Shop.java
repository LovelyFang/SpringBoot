package com.tsxy.completablefuture.entity;

import com.tsxy.completablefuture.enums.CodeEnum;
import com.tsxy.completablefuture.utils.FutureUtil;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @Author Liu_df
 * @Date 2023/4/3 19:51
 */
public class Shop {

    private String name;

    public Shop() {
    }

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Random random = new Random();

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getDisCountPrice(String product) {

        double price = calculatePrice(product);
        CodeEnum code = CodeEnum.values()[random.nextInt(CodeEnum.values().length)];
        return String.format("%s:%.2f:%s", product, price, code);
    }

    public Future<Double> getPriceAsync(String product) {
        /*CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread( () -> {
            try {
                double price = calculatePrice(product);
                future.complete(price);
            } catch (Exception e) {
                future.completeExceptionally(e);    // 将异常抛出, 使调用方知道发生了什么问题
            }
        }).start();
        return future;*/

        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }


    /**
     * 第三方服务, 计算商品价格
     */
    private double calculatePrice(String product) {
        FutureUtil.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

}
