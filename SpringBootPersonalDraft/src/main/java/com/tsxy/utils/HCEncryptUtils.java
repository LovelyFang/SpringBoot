package com.tsxy.utils;

import com.gzhc365.component.security.api.SecurityFactory;
import com.gzhc365.component.security.api.Sign;
import com.gzhc365.component.security.api.SignType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 海鹚的那套加密规则
 * @Author Liu_df
 * @Date 2023/3/30 11:05
 */
public class HCEncryptUtils {

    private static final Logger logger = LoggerFactory.getLogger(HCEncryptUtils.class);

    public static <K extends Comparable<K>> String getMD5Value(Map<K, ?> map, String partnerSecret) {
        String strA = joinKeyAndValueWithSort(map, true);
//        String strTmp = strA + "&key=" + key;
        String strTmp = strA + "&" + partnerSecret;
        logger.debug("sign originStr :{}" ,strTmp);
        return getMd5(strTmp);
    }

    public static String getMd5(String  str){
        try {
            Sign sign = SecurityFactory.getSignInstance(SignType.MD5, null);
            return sign.sign(str.getBytes()).toUpperCase();
        } catch (Exception e) {
            logger.error("SignUtils Md5 error ", e);
            return null;
        }
    }

    public static <K extends Comparable<K>> String joinKeyAndValueWithSort(Map<K, ?> map, boolean removeBlank) {
        List<K> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder builder = new StringBuilder(1000);
        for (int i = 0; i < keyList.size(); i++) {
            K key = keyList.get(i);
            if (StringUtils.equalsIgnoreCase(String.valueOf(key), "password")) {
                continue;
            }
            Object value = map.get(key);
            if (removeBlank && (null == value || "".equals(value))) {
                continue;
            }
            builder.append(key);
            builder.append("=");
            if (null != map.get(key)) {
                logger.debug("key:{},value:{}",key,value.toString());
                builder.append(value);
            }
            if (i < keyList.size() - 1) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

}
