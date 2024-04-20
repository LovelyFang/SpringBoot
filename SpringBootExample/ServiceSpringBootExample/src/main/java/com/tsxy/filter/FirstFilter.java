package com.tsxy.filter;

import com.tsxy.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @Author Liu_df
 * @Date 2023/11/6 11:14
 */

//@WebFilter(filterName = "第一个过滤器", urlPatterns = "/health/*")
@Component
public class FirstFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(FirstFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        logger.info("=====第一个过滤器=====");

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            String header = request.getHeader(s);
            logger.info("====>header:{}", header);
        }
        String ipAddress = CommonUtils.getIpAddress(request);
        logger.info("=====>IP: {}", ipAddress);
        filterChain.doFilter(request, response);


    }
}
