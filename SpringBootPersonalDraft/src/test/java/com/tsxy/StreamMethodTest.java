package com.tsxy;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @Author Liu_df
 * @Date 2023/3/29 14:40
 */
public class StreamMethodTest {


    final static List<Map<String, String>> listMapStringList = new ArrayList<>();
    static {
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "0001");
        map1.put("name", "张三");
        map1.put("age", "12");
        map1.put("address", "江苏南京");

        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "0002");
        map2.put("name", "李四");
        map2.put("age", "14");
        map2.put("address", "江苏无锡");


        Map<String, String> map3 = new HashMap<>();
        map3.put("id", "0003");
        map3.put("name", "王二");
        map3.put("age", "11");
        map3.put("address", "浙江台州");

        Map<String, String> map4 = new HashMap<>();
        map4.put("id", "0001");
        map4.put("name", "李五");
        map4.put("age", "12");
        map4.put("address", "浙江温州");
        listMapStringList.add(map1);
        listMapStringList.add(map2);
        listMapStringList.add(map3);
        listMapStringList.add(map4);
    }


    /**
     * #document <a href="https://blog.csdn.net/weixin_43759352/article/details/129647569">博客</a>
     */
    @Test
    public void testStream2Map(){

//        Map<String, List<String>> collect = listMapStringList.stream().collect(Collectors.toMap(item -> item.get("id"), each -> Collections.singletonList(each.get("name")), (value1, value2) -> {
        Map<String, Set<String>> collect = listMapStringList.stream().collect(Collectors.toMap(item -> item.get("id"), each -> Collections.singleton(each.get("name")), (value1, value2) -> {
//            List<String> union = new ArrayList<>(value1);
            Set<String> union = new HashSet<>(value1);
            union.addAll(value2);
            return union;
        }));
        System.out.println(JSON.toJSONString(collect));
    }


    @Test
    public void testSoreStream(){

        List<entity.PatientRequisitionVo> list = new ArrayList<>();
        entity.PatientRequisitionVo p1 = new entity.PatientRequisitionVo();
        p1.setReqDateTime("2022-12-20 12:22:32");
        list.add(p1);
        entity.PatientRequisitionVo p2 = new entity.PatientRequisitionVo();
        p2.setReqDateTime("2022-12-20 09:59:22");
        list.add(p2);
        entity.PatientRequisitionVo p3 = new entity.PatientRequisitionVo();
        p3.setReqDateTime("2022-12-12 01:22:32");
        list.add(p3);
        List<entity.PatientRequisitionVo> sortList = list.stream().sorted(Comparator.comparing(entity.PatientRequisitionVo::getReqDateTime)).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(sortList));

        // 看看这个时间是多少
        System.out.println(MINUTES.toMillis(30));

    }



}
