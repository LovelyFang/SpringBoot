package com.tsxy.completablefuture.service.impl;

import com.tsxy.completablefuture.entity.Quote;
import com.tsxy.completablefuture.enums.CodeEnum;
import com.tsxy.completablefuture.service.FutureMethod;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 异步的方法
 * @Author Liu_df
 * @Date 2023/4/3 20:14
 */
public class AsyncFutureMethod implements FutureMethod {

    @Override
    public List<String> findPrices(String product) {

        List<CompletableFuture<String>> completableFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> String.format("商品: %s 的价格是 => %.2f", shop.getName(), shop.getPrice(product)), executor))
                .collect(Collectors.toList());
        return completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }

    /**
     * <pre>
     *  thenCompose(Function&lt;? super T, ? extends CompletionStage&lt;U&gt;&gt; fn)
     *  第一个操作完成, 作为第二个操作的参数
     *
     *  thenCombine(CompletionStage&lt;? extends U&gt; other, BiFunction&lt;? super T,? super U,? extends V&gt; fn)
     *  上一次操作的结果 + other 操作的结果  ==> fn 的两入参
     */
    @Override
    public List<String> findDiscountPrice(String product) {

        /*CompletableFuture.supplyAsync(() -> shop.getDisCountPrice(product))
                .thenCombine(CompletableFuture.supplyAsync(() -> exchangeService.getRate(Money.EUR, Money.USD)),
                        (price, rate) -> price * rate);*/

        List<CompletableFuture<String>> completableFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() ->
                        shop.getDisCountPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                CodeEnum.applyDiscount(quote), executor)))
                .collect(Collectors.toList());

        return completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }

    @Override
    public Stream<CompletableFuture<String>> findDiscountPriceByRandomDelay(String product) {

        Stream<CompletableFuture<String>> futureStream = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getDisCountPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                CodeEnum.applyDiscount(quote), executor)));

        return futureStream;
    }

}
