package com.tsxy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.tsxy.entity.Constants.*;

public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void sendSuccessStringMessage(HttpServletRequest request, HttpServletResponse response, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, 0);
        result.put(MSG, message);
        writerJson(response, result);
    }

    public void sendSuccessData(HttpServletRequest request, HttpServletResponse response, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, 0);
        result.put(DATA, data);
        writerJson(response, result);
    }

    /**
     * 提示失败信息
     */
    public void sendFailureMessage(HttpServletRequest request, HttpServletResponse response,
                                   int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, code);
        result.put(MSG, message);
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
            PrintWriter out = null;
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        Map<String, String[]> req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Set<String> keys = req.keySet();
            for (String key : keys) {
                Object value = req.get(key);
                Object v = null;
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

}
