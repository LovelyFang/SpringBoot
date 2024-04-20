package com.tsxy.huToolweb;

import com.alibaba.fastjson.JSON;
import com.tsxy.dao.UserDao;
import com.tsxy.entity.User;
import com.tsxy.utils.FileUpload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuToolWebApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testHelloWorld() {
        System.out.println("Hello World");
    }

    @Test
    void testSelectById() {
        User user = userDao.selectById(1);
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    void testWriteReadFile() {
        FileUpload.writeFile("use中文.png");
    }


}
