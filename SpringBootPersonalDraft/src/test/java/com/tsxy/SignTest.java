package com.tsxy;

import com.alibaba.fastjson.JSONObject;
import com.tsxy.utils.MD5EncryptUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author Liu_df
 * @Date 2022/12/21 15:14
 */
public class SignTest {


    @Test
    public void testSign(){

        Map param = new HashMap();
        param.put("sign", "");
        param.put("users", "567,890");
        param.put("user", "1234");
        String pd123456 = MD5EncryptUtils.generateSign(param, "pd123456");
        System.out.println(pd123456);
    }

    @Test
    public void testIsEMS() {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("customerCode", "HcNxs");  // 大客户代码（由广西EMS提供）
        reqJSON.put("appid", "wx40fc40310e54c7f6");    // 项目Id，由广西EMS提供
        // reqJSON.put("redirect_url", redirect_url);   // 微信支付完成后跳转的地址；为空则跳转到EMS官微
        reqJSON.put("resulttype", "mp_weixin"); // 取值范围：url、base64、 mp_weixin、mp_alipay 默认：url
        reqJSON.put("sname", "冯玉琼");
        reqJSON.put("sphone1", "0773-3847712");
        reqJSON.put("sprovince", "广西壮族自治区");
        reqJSON.put("scity", "桂林市");
        reqJSON.put("scounty", "象山区");
        reqJSON.put("sstreet", "崇信路46号");
        reqJSON.put("saddress", "广西壮族自治区南溪山医院-病案科");
        reqJSON.put("rname", "王俊彪");    // 收件人
        reqJSON.put("rphone1", "15674742505");    // 收件电话
        reqJSON.put("rprovince", "北京市");    // 收件省份
        reqJSON.put("rcity", "北京市");    // 收件城市
        reqJSON.put("rcounty", "东城区");    // 收件区、县
        reqJSON.put("rstreet", "null");    // 收件街道
        reqJSON.put("raddress", "湖南省衡阳市祁东县公安局");  // 收件详细地址，长度：4-100
        String sign = MD5EncryptUtils.generateSign(reqJSON, "53e13f8083bb0471fcb1be87ba8c8ff6");
        System.out.println(sign);
    }


}
