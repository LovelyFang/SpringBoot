package com.tsxy.completablefuture.entity;

import com.tsxy.completablefuture.enums.CodeEnum;

/**
 * @Author Liu_df
 * @Date 2023/4/3 21:22
 */
public class Quote {

    private String shopName;
    private String price;
    private CodeEnum discountCode;

    public static Quote parse(String s) {
        String[] split = s.split(":");
        String shopName = split[0];
        String price = split[1];
        CodeEnum discountCode = CodeEnum.valueOf(split[2]);
        return new Quote(shopName, price, discountCode);
    }

    public Quote() {
    }

    public Quote(String shopName, String price, CodeEnum discountCode) {
        this.shopName = shopName;
        this.price = price;
        this.discountCode = discountCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CodeEnum getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(CodeEnum discountCode) {
        this.discountCode = discountCode;
    }
}
