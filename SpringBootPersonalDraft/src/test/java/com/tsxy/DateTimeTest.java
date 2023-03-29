package com.tsxy;

import com.gzhc365.component.utils.common.DateTool;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author Liu_df
 * @Date 2023/3/28 15:26
 */
public class DateTimeTest {

    private static final Logger logger = LoggerFactory.getLogger(demo.class);

    /**
     * 使用 LocalDate 比较两个日期间隔天数
     */
    @Test
    public void testCompareTwoLocalDateIntervals(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginLocalDate = LocalDate.parse("2023-01-13", dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse("2023-03-13", dateTimeFormatter);
        long until = beginLocalDate.until(endLocalDate, ChronoUnit.DAYS);
        boolean intervals = until > 90;
        System.out.println(until);
        System.out.println(intervals);
    }


    /**
     * 日期加1
     */
    @Test
    public void testTimeAdd() throws ParseException {
        String time = "2022-10-21";
        Date beforeTime = DateTool.getFullDate().parse(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(beforeTime);
        instance.add(Calendar.DATE, 1);
        Date afterTime = instance.getTime();
        String afterTimeStr = DateTool.getFullDate().format(afterTime);
        System.out.println(afterTimeStr);
    }


}
