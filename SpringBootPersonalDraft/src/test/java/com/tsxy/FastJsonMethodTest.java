package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author Liu_df
 * @Date 2023/3/29 15:24
 */
public class FastJsonMethodTest {

    @Test
    public void testIsEmpty() throws IOException, JDOMException {

        String request = "{\"ResultCode\":\"0\",\"ResultMsg\":\"执行成功\",\"totalFeeList\":[]}";

        JSONObject jsonObject = JSONObject.parseObject(request);
        JSONArray totalFeeList = jsonObject.getJSONArray("totalFeeList");
        System.out.println(totalFeeList.isEmpty());
    }

    /**
     * parseObject方法
     */
    @Test
    public void test(){

        JSONObject response = new JSONObject();
        response.put("outpatientno", "7822222");
        response.put("patcardtype", "01");
        response.put("patcardno", "23669999");
        response.put("patientname", "梁朝荣");
        response.put("serialnumber", "W000025");
        response.put("patid", "2365");
        response.put("prescriptionno", "");
        response.put("orderid", "");
        response.put("entryname", "");
        response.put("registrationtime", "2022-11-01 10:20:35");
        response.put("isMedicalPatient", "true");

        entity.OutpatientWaitPayVO outpatientWaitPayVO = JSONObject.parseObject(JSON.toJSONString(response), entity.OutpatientWaitPayVO.class);
        System.out.println(outpatientWaitPayVO);
    }

}
