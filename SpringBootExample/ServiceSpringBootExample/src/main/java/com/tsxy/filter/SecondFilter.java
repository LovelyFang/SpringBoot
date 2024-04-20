package com.tsxy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Liu_df
 * @Date 2023/11/6 11:14
 */

//@WebFilter(filterName = "第二个过滤器", urlPatterns = "/health/*")
//@Order(1)
@Component
public class SecondFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SecondFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        logger.info("=====第二个过滤器=====");
        filterChain.doFilter(request, response);


    }
}
