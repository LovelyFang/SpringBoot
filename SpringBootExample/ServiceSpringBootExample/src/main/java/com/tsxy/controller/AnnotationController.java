package com.tsxy.controller;

import com.tsxy.annotation.Log;
import com.tsxy.constants.BusinessTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:33
 */
@Slf4j
@RestController
@RequestMapping("/annotation")
public class AnnotationController {

    @Log(title = "测试呢", businessType = BusinessTypeEnum.INSERT)
    @GetMapping("/saveLog")
    public void saveLog(){
        log.info("我就是来测试一下是否成功！");
    }

}
