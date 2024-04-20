package com.tsxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.tsxy.dao"})
public class SpringBootExampleTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootExampleTestApplication.class, args);
    }

}
