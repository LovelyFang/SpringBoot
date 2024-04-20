package com.tsxy.scheduledtasks.controller;

import com.tsxy.scheduledtasks.utils.HtmlUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public final static String CODE = "code";

    public final static String MSG = "msg";

    public final static String DATA = "data";

    public void sendSuccessMessage(HttpServletRequest request, HttpServletResponse response, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, 0);
        result.put(MSG, message);
        HtmlUtil.writerJson(response, result);
    }

    public void sendSuccessData(HttpServletRequest request, HttpServletResponse response, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, 0);
        result.put(DATA, data);
        HtmlUtil.writerJson(response, result);
    }

    public void sendFailureMessage(HttpServletRequest request, HttpServletResponse response, int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put(CODE, code);
        result.put(MSG, message);
        HtmlUtil.writerJson(response, result);
    }
    
}
