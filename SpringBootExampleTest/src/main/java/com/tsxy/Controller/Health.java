package com.tsxy.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

}
