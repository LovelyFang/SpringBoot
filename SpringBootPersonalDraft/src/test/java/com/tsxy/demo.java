package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.DeptRegSourceVo;
import entity.GetDeptRegSourceVo;
import org.dom4j.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author Liu_df
 * @Date 2022/9/25 9:46
 */
public class demo {

    private static final Logger logger = LoggerFactory.getLogger(demo.class);

    private static final String REQUEST_BODY = "data:{" +
            "first:${patientName}\n" +
            "second:${hisOrderNo}\n" +
            "third:${checkTime}\n" +
            "}";

    @Test
    public void test45syncJob(){
        List<Map<String, Object>> orders = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("patientName", "刘登方1");
        params.put("hisOrderNo", "123456789");
        params.put("checkTime", "2022-10-09");
        orders.add(params);
        Map<String, Object> params1 = new HashMap<>();
        params1.put("patientName", "刘登方2");
        params1.put("hisOrderNo", "123456");
        params1.put("checkTime", "2022-10-06");
        orders.add(params1);
        Map<String, Object> params2 = new HashMap<>();
        params2.put("patientName", "刘登方3");
        params2.put("hisOrderNo", "123");
        params2.put("checkTime", "2022-10-03");
        orders.add(params2);
        for (Map order : orders){
            String messageContent = getMessageContent(REQUEST_BODY, order);
            System.out.println(messageContent);
        }
    }

    public static String getMessageContent(String content, Map<String, Object> valueMap) {
        if (valueMap == null) {
            return content;
        }
        if (content == null) {
            return content;
        }
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(content);
        Set<String> keySet = new HashSet<>();
        while (matcher.find()) {
            keySet.add(matcher.group());
        }
        for (String str : keySet) {
            String key = str.substring(2, str.length() - 1);
            String temp = "\\$\\{" + key + "\\}";
            content = content.replaceAll(temp, valueMap.get(key) == null ? "" : String.valueOf(valueMap.get(key)));
        }
        return content;
    }

    @Test
    public void testNanoTime(){
        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());
        System.out.println(Runtime.getRuntime().availableProcessors());
    }



}
