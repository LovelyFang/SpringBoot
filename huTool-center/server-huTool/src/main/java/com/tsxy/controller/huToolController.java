package com.tsxy.controller;

import com.tsxy.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liu_df
 * @Date 2023/4/6 18:16
 */
@RestController
@RequestMapping("/test")
public class huToolController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/saveLog")
    public void saveLog(){

        userDao.selectById(1);

    }

}
