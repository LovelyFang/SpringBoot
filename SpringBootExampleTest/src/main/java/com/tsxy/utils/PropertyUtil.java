package com.tsxy.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
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
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            logger.info("读取配置文件路径==>{}", path + PROPERTY_FILE);
            in = new FileInputStream(path + PROPERTY_FILE);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            props.load(bf);
            // 方法二
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
