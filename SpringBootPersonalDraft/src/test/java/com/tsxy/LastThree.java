package com.tsxy;

import com.mchange.lang.IntegerUtils;
import org.junit.Test;

/**
 * @Author Liu_df
 * @Date 2022/12/22 18:02
 */
public class LastThree {


    public int getLastThree(int num){

        if (num == 1 || num == 2){
            return num;
        }
        int result = num * getLastThree(num - 2);
        String resultStr = Integer.toString(result);
        if (resultStr.length() > 3) {
            resultStr = resultStr.substring(resultStr.length()-3);
        }
        result = Integer.parseInt(resultStr);
        return result;
    }

    /**
     * 数字相乘后最后三位
     */
    @Test
    public void test(){
        int lastThree = getLastThree(1991);
        System.out.println(lastThree);
    }


}
