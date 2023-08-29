package com.tsxy;

import com.gzhc365.component.utils.common.DateTool;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Instant instant = Instant.ofEpochMilli(1689297298000L);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime beforeLocalDateTime = LocalDateTime.ofInstant(instant, zoneId);

        LocalDateTime now = LocalDateTime.now();
        long until1 = beforeLocalDateTime.until(now, ChronoUnit.DAYS);
        boolean ine = until1 > 6;
        System.out.println(until1);
        System.out.println(ine);

        String specification = "0.4ml*0.4万*2.3支/盒";
        specification = "1g*100ml*";
        specification = "100mg*12片(8#)";
        String[] specificationArr = specification.split("\\*");
        String multiplier = specificationArr[specificationArr.length-1];
        multiplier = multiplier.replaceAll("\\(.*?\\)", "");
        multiplier =  multiplier.replaceAll("[^\\d.]", "");
        double multiplierInt = Double.parseDouble(multiplier);
        System.out.println(multiplierInt);
        System.out.println(24 % multiplierInt != 0);

        String rurl = "<a href=http://192.168.20.232:8081/Uploads/TempPDF20230821/ab385e8d-6_20230817104238邵明江_ABP.pdf</a>";
        Pattern pattern = Pattern.compile("<a href=.*</a>");
        Matcher matcher = pattern.matcher(rurl);
        while (matcher.find()){
            System.out.println(matcher.group());
        }

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
