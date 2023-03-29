package com.tsxy;

import com.alibaba.fastjson.JSONObject;
import com.tsxy.entity.BaseMpVo;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author Liu_df
 * @Date 2023/3/29 15:22
 */
public class BigDecimalMethodTest {


    /**
     * BigDecimal.multiply()
     */
    @Test
    public void testBigDecimal() {
        String s = new BigDecimal("0.3331").multiply(new BigDecimal("2")).setScale(2, RoundingMode.HALF_UP).toString();
        System.out.println(s);
    }



}
