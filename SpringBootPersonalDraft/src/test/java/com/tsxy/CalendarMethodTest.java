package com.tsxy;

import com.gzhc365.component.utils.common.DateTool;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author Liu_df
 * @Date 2023/3/29 15:55
 */
public class CalendarMethodTest {

    @Test
    public void testCalendarCompare(){

        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.MARCH, 13, 14, 27 ,49);
        Date time = cal.getTime();
        String format = DateTool.getFullDateTime().format(time);
        System.out.println(format);
        if (System.currentTimeMillis() - time.getTime() < Integer.parseInt("1440") * 60000){
            System.out.println("hahahahhahahahaa");
        }

    }

}
