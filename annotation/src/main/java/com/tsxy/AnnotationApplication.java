package com.tsxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:29
 */
@SpringBootApplication
//@MapperScan(basePackages = "com.tsxy.mapper")
public class AnnotationApplication {

    public static void main(String[] args) {

        SpringApplication.run(AnnotationApplication.class, args);

    }

}
