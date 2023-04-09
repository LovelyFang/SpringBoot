package com.tsxy.java8time.localdatetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

/**
 * @Author Liu_df
 * @Date 2023/4/5 11:19
 */
public class LocalDateTimeAPI {


    public static void main(String[] args) {

        LocalDateTimeAPI localDateTimeAPI = new LocalDateTimeAPI();
        localDateTimeAPI.LocalDateAPI();
        
    }

    public void LocalDateAPI() {

        LocalDate todayLocalDate = LocalDate.now();
        System.out.println("时间:" + todayLocalDate);
        System.out.println("getXxx获取年份月份:" + todayLocalDate.getMonth() + " 数字:" + todayLocalDate.getMonthValue());
        System.out.println("get获取日期:" + todayLocalDate.get(ChronoField.DAY_OF_MONTH));;
        System.out.println("withXxx更改年份(之前的不变):" + todayLocalDate.withYear(2024));
//        System.out.println(todayLocalDate.with(ChronoField.HOUR_OF_DAY, 10)); // 这个修改时间会报错
        System.out.println("with今年第十个这个星期是:" + todayLocalDate.with(ChronoField.ALIGNED_WEEK_OF_YEAR, 10));  // 展示今年第几个这个星期
        System.out.println("plusXxx加一年:" + todayLocalDate.plusYears(1));
        System.out.println("plus加五月:" + todayLocalDate.plus(5, ChronoUnit.MONTHS));
        System.out.println("isAfter判断先后:" + todayLocalDate.isAfter(todayLocalDate.plus(5, ChronoUnit.MONTHS)));
        System.out.println("parse成LocalDate:" + LocalDate.parse("2023-04-05", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println("format成String:" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(todayLocalDate));
    }



}
