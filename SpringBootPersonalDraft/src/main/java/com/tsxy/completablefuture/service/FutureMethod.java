package com.tsxy.completablefuture.service;

import com.tsxy.completablefuture.entity.Shop;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * @Author Liu_df
 * @Date 2023/4/3 20:11
 */
public interface FutureMethod {

    List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("MyFavoriteShop"),
            new Shop("BugItAll"),
            new Shop("LetsSaveBig")/*,
            new Shop("BestPrice"),
            new Shop("MyFavoriteShop"),
            new Shop("BugItAll"),
            new Shop("LetsSaveBig"),
            new Shop("BestPrice"),
            new Shop("MyFavoriteShop"),
            new Shop("BugItAll"),
            new Shop("LetsSaveBig"),
            new Shop("BestPrice"),
            new Shop("MyFavoriteShop"),
            new Shop("BugItAll"),
            new Shop("LetsSaveBig"),
            new Shop("BestPrice"),
            new Shop("MyFavoriteShop"),
            new Shop("BugItAll"),
            new Shop("LetsSaveBig")*/);

    Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100),
            r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });


    List<String> findPrices(String product);

    List<String> findDiscountPrice(String product);

    Stream<CompletableFuture<String>> findDiscountPriceByRandomDelay(String product);


}
