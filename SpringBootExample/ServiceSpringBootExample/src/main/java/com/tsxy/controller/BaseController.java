package com.tsxy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tsxy.constants.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 获取请求参数
     * 表单形式
     */
    public Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        if (request == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            if (servletRequestAttributes != null) {
                request = servletRequestAttributes.getRequest();
            }else {
                return result;
            }
        }
        Map<String, String[]> req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Set<String> keys = req.keySet();
            for (String key : keys) {
                Object value = req.get(key);
                Object v;
                if ((value.getClass().isArray()) && (((Object[]) value).length > 0)) {
                    v = ((Object[]) value)[0];  // 多个就取第一个
                } else {
                    v = value;
                }
                if (((v instanceof String))) {
                    String s = ((String) v).trim();
                    if (s.length() > 0) {
                        result.put(key, s);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取请求参数
     * JSON格式
     */
    public Map<String, Object> getJSON(HttpServletRequest request) throws Exception {
        BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return JSONObject.parseObject(responseStrBuilder.toString(), Map.class);
    }


    /**
     * 返回成功消息
     */
    public void sendSuccessStringMessage(HttpServletResponse response, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(BaseConstants.CODE, 0);
        result.put(BaseConstants.MSG, message);
        writerJson(response, result);
    }

    /**
     * 放回成功数据
     */
    public void sendSuccessData(HttpServletResponse response, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put(BaseConstants.RESULT_CODE_SUCCESS, 0);
        result.put(BaseConstants.DATA, data);
        writerJson(response, result);
    }

    /**
     * 提示失败信息
     */
    public void sendFailureMessage(HttpServletResponse response, int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(BaseConstants.CODE, code);
        result.put(BaseConstants.MSG, message);
        writerJson(response, result);
    }

    public static void writerJson(HttpServletResponse response, Object object) {
        response.setContentType("application/json");
        String json = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        logger.info("Response:{}", json);
        writer(response, json);
    }

    private static void writer(HttpServletResponse response, String str) {
        try {
            // 设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
