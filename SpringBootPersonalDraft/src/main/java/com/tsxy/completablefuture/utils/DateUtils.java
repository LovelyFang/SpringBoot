package com.tsxy.completablefuture.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author Liu_df
 * @Date 2023/6/1 17:05
 */
public class DateUtils {

    /**
     * 获取LocalDate对象
     *
     * @param date
     * @return LocalDate对象
     */
    public static LocalDate getLocalDateFromDate(Date date) {
        // 将Date转为Instant对象
        Instant instant = date.toInstant();

        // 默认时区
        ZoneId zoneId = ZoneId.systemDefault();

        // 获取LocalDate对象
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        return localDate;
    }

}
