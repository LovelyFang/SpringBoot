package com.tsxy.completablefuture.enums;

import com.tsxy.completablefuture.entity.Quote;
import com.tsxy.completablefuture.utils.FutureUtil;

/**
 * @Author Liu_df
 * @Date 2023/4/3 21:24
 */
public enum CodeEnum {

    NONE(0),
    SILVER(5),
    GOLD(10),
    PLATINUM(15),
    DIAMOND(20);

    private final int percentage;

    CodeEnum(int percentage) {
        this.percentage = percentage;
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " 价格为: " + apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static String apply(String price, CodeEnum discountCode) {
        FutureUtil.delay();
        return (Double.parseDouble(price) * (100 - discountCode.percentage) / 100) + "";
    }

}
