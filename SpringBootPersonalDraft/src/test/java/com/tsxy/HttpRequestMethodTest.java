package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP请求方法测试类
 * @Author Liu_df
 * @Date 2023/12/6 18:35
 */
public class HttpRequestMethodTest {

    @Test
    public void testRestTemplate(){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("hisId", "2449");
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        String requestAddress = "http://112.91.80.19:23024/api/customize/his/queryPayResult";
        ResponseEntity<Object> response = restTemplate.postForEntity(requestAddress, entity, Object.class);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.value() == HttpStatus.OK.value()) {
            System.out.println(JSON.toJSONString(response));
        }
    }

}
