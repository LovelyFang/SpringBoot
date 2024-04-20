package com.tsxy.completablefuture.service.impl;

import com.tsxy.completablefuture.entity.Quote;
import com.tsxy.completablefuture.enums.CodeEnum;
import com.tsxy.completablefuture.service.FutureMethod;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 同步的方法
 * @Author Liu_df
 * @Date 2023/4/3 20:14
 */
public class SyncFutureMethod implements FutureMethod {

    @Override
    public List<String> findPrices(String product) {

//        return shops
//                .stream()
//                .map(shop -> String.format("商品: %s 的价格是 => %.2f", shop.getName(), shop.getPrice(product)))
//                .collect(Collectors.toList());

        return shops.parallelStream()
                .map(shop -> String.format("商品: %s 的价格是 => %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findDiscountPrice(String product) {

        return shops.stream()
                .map(shop -> shop.getDisCountPrice(product))
                .map(Quote::parse)
                .map(CodeEnum::applyDiscount)
                .collect(Collectors.toList());
    }

    @Override
    public Stream<CompletableFuture<String>> findDiscountPriceByRandomDelay(String product) {
        return null;
    }
}
