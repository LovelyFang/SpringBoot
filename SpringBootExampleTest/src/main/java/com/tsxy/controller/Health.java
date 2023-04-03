package com.tsxy.controller;

import com.tsxy.utils.PropertyUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @Author Liu_df
 * @Date 2022/11/15 0:08
 */

@RestController
public class Health {

    @RequestMapping("/health")
    public String health() {
        return "hello world";
    }

    @RequestMapping("/getProperty/{key}")
    public String getProperty(@PathVariable String key) {
        String property = PropertyUtil.getProperty(key);
        return property;
    }

}
