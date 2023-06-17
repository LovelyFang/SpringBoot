package com.tsxy.utils.medical;

import com.alibaba.fastjson.JSON;
import com.gzhc365.component.security.api.SecurityFactory;
import com.gzhc365.component.security.api.Sign;
import com.gzhc365.component.security.api.SignType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author Liu_df
 * @Date 2023/5/12 10:57
 */
public class RefreshTestZhyyEnvYbAccessTokenUtil {

    private static Logger logger = LoggerFactory.getLogger(RefreshTestZhyyEnvYbAccessTokenUtil.class);

    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        String timeStamp = System.currentTimeMillis() + "";
        params.put("platformId", "2155");
        params.put("hisId", "2155");
        params.put("platformSource", "3");
        params.put("subSource", "1");//
        params.put("timestamp", timeStamp);
        params.put("ver", "1");
        System.out.println("params："+ JSON.toJSONString(params));
        String sign = getHaiciSign(params, "xG1h@C(kc0MV^Res#vYiFKiw%cHIz&wn.oW$3eSEJ");

        System.out.println("sign ： " + sign);

    }

    public static String getHaiciSign(Map<String, Object> map, String key) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (null != entry.getValue() && StringUtils.isNotBlank(entry.getValue() + "")) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        if (StringUtils.isNotBlank(key)) {
            sb.append(key);
        }
        String result = "";
        try {
            result = new String(sb.toString().getBytes(), "UTF-8");
            Sign sign = SecurityFactory.getSignInstance(SignType.MD5, null);
            logger.info("加密前为 :{}", result);
            result = sign.sign(result.getBytes()).toUpperCase();
            logger.info("加密后为 :{}", result);
        } catch (Exception e) {
            logger.info("出问题啦{}", e);

        }
        return result;
    }
}
