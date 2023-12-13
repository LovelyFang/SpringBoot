package com.tsxy.config;

import com.tsxy.filter.FirstFilter;
import com.tsxy.filter.SecondFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 统一管理项目的过滤器
 * 这种方式更好管理。
 * @Author Liu_df
 * @Date 2023/11/6 11:29
 */

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<FirstFilter> getFirstFilterBean(FirstFilter firstFilter) {
        FilterRegistrationBean<FirstFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(firstFilter);
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/health/*");
        filterRegistrationBean.setName("firstFilter");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<SecondFilter> getSecondFilterBean(SecondFilter secondFilter) {
        FilterRegistrationBean<SecondFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(secondFilter);
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/health/*");
        filterRegistrationBean.setName("secondFilter");
        return filterRegistrationBean;
    }





}
