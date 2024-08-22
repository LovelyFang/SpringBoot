package com.tsxy.scheduledtasks.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author Liu_df
 * @Date 2024/8/22 10:41
 */
public class LogMDCFilter implements Filter {

    private static final String UNIQUE_ID_NAME = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put(UNIQUE_ID_NAME, "tsxy_" + UUID.randomUUID().toString().replace("-", ""));
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(UNIQUE_ID_NAME);
        }
    }
}
