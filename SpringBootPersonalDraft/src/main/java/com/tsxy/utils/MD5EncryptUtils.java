package com.tsxy.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author Liu_df
 * @Date 2023/3/29 15:07
 */
public class MD5EncryptUtils {

    /**
     * 生成签名
     * @param param 接口参数
     * @param secretKey 密匙
     * @return 生成的签名
     * */
    public static String generateSign(Map param, String secretKey){
        String result = "";
        if(param != null && param.size() > 0) {
            TreeMap<String,Object> treeParam = new TreeMap<>();
            treeParam.putAll(param);
            treeParam.remove("sign");
            //组装待签名字符串
            StringBuilder waitSign = new StringBuilder();
            for(String keyTemp : treeParam.keySet()){
                waitSign.append(keyTemp);
                waitSign.append(treeParam.get(keyTemp));
            }
            System.out.println("待签名字符串：" + waitSign.toString());
            //对待签名字符串进行md5处理
            waitSign = new StringBuilder(DigestUtils.md5Hex(waitSign.toString()));
            System.out.println("MD5待签名字符串：" + waitSign.toString());
            //对md5处理过的待签名字符串添加secretKey
            waitSign.append(secretKey);
            System.out.println("MD5待签名字符串+secretKey：" + waitSign.toString());
            //再次进行md5处理
            waitSign = new StringBuilder(DigestUtils.md5Hex(waitSign.toString()));
            System.out.println("再次MD5待签名字符串：" + waitSign.toString());
            result = waitSign.toString();
        }
        return result;
    }

}
