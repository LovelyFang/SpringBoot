package com.tsxy.scheduledtasks.controller;

import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.front.handler.ElkLogPreFilter;
import com.gzhc365.component.front.web.utils.FrontUtils;
import com.gzhc365.component.utils.entity.HcContext;
import com.gzhc365.web.vo.SessionUser;
import com.tsxy.scheduledtasks.utils.HtmlUtil;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class BaseController {

    public final static String CODE = "code";

    public final static String MSG = "msg";

    public final static String DATA = "data";
    
    public final static String REDIS_SESSION_USER = "redis_session_user";

    /**
     *
     * 提示成功信息
     *
     * @param message
     *
     */
    public void sendSuccessMessage(HttpServletRequest request,HttpServletResponse response, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        HcContext context = FrontUtils.getHcContext(request);
        result.put(CODE, 0);
        result.put(MSG, message);
        HtmlUtil.writerJson(response, result,context);
    }

    public void sendSuccessData(HttpServletRequest request,HttpServletResponse response, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        HcContext context = FrontUtils.getHcContext(request);
        result.put(CODE, 0);
        result.put(DATA, data);
        HtmlUtil.writerJson(response, result,context);
    }

    /**
     *
     * 提示失败信息
     *
     * @param message
     *
     */
    public void sendFailureMessage(HttpServletRequest request,HttpServletResponse response, int code, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        HcContext context = FrontUtils.getHcContext(request);
        result.put(CODE, code);
        result.put(MSG, message);
        HtmlUtil.writerJson(response, result,context);
    }
    
    
    public HcContext getHcContext(HttpServletRequest request) {
    	String uuid = UUID.randomUUID().toString();
        HcContext context = new HcContext(uuid);
        request.setAttribute(FrontUtils.REQUEST_HCCONTEXT,JSONObject.toJSONString(context));
        MDC.put(ElkLogPreFilter.REQ_SEQ, uuid);
        return context;
    }
    

    public SessionUser getUser(HttpSession session) {
        return JSONObject.parseObject(session.getAttribute(REDIS_SESSION_USER).toString(), SessionUser.class);
    }
    
}
