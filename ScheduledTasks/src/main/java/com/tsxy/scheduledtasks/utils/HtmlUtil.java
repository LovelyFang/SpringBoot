package com.tsxy.scheduledtasks.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author dengzhineng
 * @date 2016年10月11日
 */
public class HtmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

	public static void writerJson(HttpServletResponse response, Object object) {
		response.setContentType("application/json");
		String json = JSON.toJSONString(object);
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
}
