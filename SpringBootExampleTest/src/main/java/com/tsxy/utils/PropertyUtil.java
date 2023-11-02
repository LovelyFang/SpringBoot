package com.tsxy.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.ResourceReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;
    private static final String PROPERTY_FILE = "common.properties";

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
//             方法一 getClassLoader 不用加 /
//            in = ResourceReader.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
//            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            props.load(bf);

            // 方法二 class 要加 /
//            in = ResourceReader.class.getResourceAsStream("/" + PROPERTY_FILE);
//            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            props.load(bf);

            // 方法三 路径不能带转义字符
//            String path = Thread.currentThread().getClass().getClassLoader().getResource("").getPath();
            // 方法四
            /*
             *路径不能带转义字符
             * 不带 / :
             *  file:/D:/maven_repository/org/springframework/boot/spring-boot/2.7.5/spring-boot-2.7.5.jar!/org/springframework/boot/web/embedded/tomcat/common.properties
             * 带 / :
             *  /D:/IDEA Java Workspace/SpringBoot/SpringBootExampleTest/target/classes/common.properties
             */
            String path = Thread.currentThread().getContextClassLoader().getClass().getResource("/").getPath();
            path = path + PROPERTY_FILE;
            path = path.replace("%20", " ");
            logger.info("读取配置文件路径==>{}", path);
            in = new FileInputStream(path);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            props.load(bf);

            // 方法五
//            Resource resource = new ClassPathResource("common.properties");
//            props.load(resource.getInputStream());
        } catch (FileNotFoundException e) {
            logger.error("common.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成，props：{}", JSONObject.toJSONString(props));
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    /**
     * 重新加载属性文件
     */
    public static void reloadPropertyFile() {
        loadProps();
    }
}
