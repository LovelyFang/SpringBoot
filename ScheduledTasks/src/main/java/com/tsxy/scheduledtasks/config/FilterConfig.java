package com.tsxy.scheduledtasks.config;

import com.tsxy.scheduledtasks.filter.LogMDCFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Liu_df
 * @Date 2024/8/22 11:01
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogMDCFilter> logFilterRegistration() {
        FilterRegistrationBean<LogMDCFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogMDCFilter());
        registration.addUrlPatterns("/*");  // 配置过滤规则
        registration.setName("logMDCFilter");
        registration.setOrder(0);   // order的数值越小 则优先级越高
        return registration;
    }
}
