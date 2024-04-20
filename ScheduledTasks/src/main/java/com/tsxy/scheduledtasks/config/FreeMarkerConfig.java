package com.tsxy.scheduledtasks.config;

import freemarker.ext.beans.BeansWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author Liu_df
 * @Date 2024/4/16 11:58
 */
@Component
public class FreeMarkerConfig {

    @Bean("viewResolver")
    public FreeMarkerViewResolver freeMarkerViewResolver(){
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
        freeMarkerViewResolver.setRequestContextAttribute("request");
        /*
         * 在JSP和Freemarker的配置项中都有一个order property
         * 把freemarker的order设置为0, jsp为1
         * 其含义为找view时，先找ftl文件，再找jsp文件做为视图。
         */
        freeMarkerViewResolver.setOrder(0);
        freeMarkerViewResolver.setSuffix(".ftl");
        return freeMarkerViewResolver;
    }

    @Bean("freemarkerConfig")
    public FreeMarkerConfigurer freeMarkerConfigurer(){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/templates/");
        Map<String,Object> freemarkerVariables = new HashMap<>();
        freemarkerVariables.put("statics",(BeansWrapper.getDefaultInstance().getStaticModels()));
        freeMarkerConfigurer.setFreemarkerVariables(freemarkerVariables);
        Properties freemarkerSettings = new Properties();
        freemarkerSettings.setProperty("template_update_delay", "3600");
        freemarkerSettings.setProperty("locale", "zh_CN");
        freemarkerSettings.setProperty("default_encoding", "UTF-8");
        freemarkerSettings.setProperty("tag_syntax", "auto_detect");
        freemarkerSettings.setProperty("classic_compatible", "true");
        freemarkerSettings.setProperty("date_format", "yyyy-MM-dd");
        freemarkerSettings.setProperty("time_format", "HH:mm:ss");
        freemarkerSettings.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        freemarkerSettings.setProperty("number_format", "##0.##");
        freeMarkerConfigurer.setFreemarkerSettings(freemarkerSettings);
        return freeMarkerConfigurer;
    }
}
