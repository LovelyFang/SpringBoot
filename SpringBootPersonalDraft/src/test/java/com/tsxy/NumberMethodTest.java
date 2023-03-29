package com.tsxy;

import org.junit.Test;

/**
 * @Author Liu_df
 * @Date 2022/12/26 14:35
 */
public class NumberMethodTest {

    /**
     * Integer.parseInt() 方法 第二个参数含义： 进制
     */
    @Test
    public void test222(){
        int i = Integer.parseInt("0010", 2);
        System.out.println(i);
    }

}
