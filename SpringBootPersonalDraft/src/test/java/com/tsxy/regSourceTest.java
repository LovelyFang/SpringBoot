package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.utils.common.DateTool;
import com.gzhc365.component.utils.common.MoneyCovert;
import entity.DeptRegSourceVo;
import entity.GetDeptRegSourceVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * @Author Liu_df
 * @Date 2022/10/11 22:56
 */

public class regSourceTest {

    private static GetDeptRegSourceVo getDeptRegSourceVo = new GetDeptRegSourceVo();

    static {
        List<DeptRegSourceVo> regSourceVos = new ArrayList<>();
        DeptRegSourceVo deptRegSourceVo = new DeptRegSourceVo();
        deptRegSourceVo.setDeptNo("123");
        deptRegSourceVo.setDoctorNo("123456");
        deptRegSourceVo.setLeftNum(4);
        regSourceVos.add(deptRegSourceVo);
        DeptRegSourceVo deptRegSourceVo1 = new DeptRegSourceVo();
        deptRegSourceVo1.setDeptNo("456");
        deptRegSourceVo1.setDoctorNo("456789");
        deptRegSourceVo1.setLeftNum(7);
        regSourceVos.add(deptRegSourceVo1);
        DeptRegSourceVo deptRegSourceVo2 = new DeptRegSourceVo();
        deptRegSourceVo2.setDeptNo("789");
        deptRegSourceVo2.setDoctorNo("789123");
        deptRegSourceVo2.setLeftNum(0);
        regSourceVos.add(deptRegSourceVo2);
        getDeptRegSourceVo.setRegSources(regSourceVos);
    }

    @Test
    public void sourceListTest(){

        List<DeptRegSourceVo> deptRegSourceVos = getDeptRegSourceVo.getRegSources();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("abc");
        arrayList.add("edf");
        for (int i = 0; i < arrayList.size(); i++) {
            Map<String, String> extProps = new HashMap<>();
            extProps.put("doctorCode",arrayList.get(i));
            deptRegSourceVos.get(i).setExtPropes(extProps);
        }
        System.out.println(JSON.toJSONString(deptRegSourceVos));

    }

}
