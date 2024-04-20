package com.tsxy.controller;

import com.alibaba.fastjson.JSON;
import com.tsxy.dao.UserDao;
import com.tsxy.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liu_df
 * @Date 2023/4/6 18:16
 */
@RestController
@RequestMapping("/user/userInfo")
public class UserInfoController {

    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserDao userDao;

    @GetMapping("/saveLog")
    public void saveLog(){
        User user = userDao.selectById(1);
        logger.info(JSON.toJSONString(user));
    }


}
