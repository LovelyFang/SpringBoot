package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tsxy.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Map;

/**
 * 海鹚加密相关测试类
 * @Author Liu_df
 * @Date 2022/12/21 15:14
 */
public class SignTest {

    /**
     * 商户后台手机加密算法
     */
    @Test
    public void testHCSecurityTool() {
        String ehis = null;
        try {
            ehis = SecurityTool.aes_encrypt("13789198919", "ehis");
            String ehis1 = SecurityTool.aes_decrypt("27e709c6c03061ab5346116fb469653f", "ehis");
            System.out.println("数据解密为===> " + ehis1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(ehis);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        System.out.println(JSON.toJSONString(stackTrace));
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    /**
     * 商户后台密码加密算法  密码 + `hc_oauth_center`.`t_oc_user` 表 id 字段
     */
    @Test
    public void testHCMd5Encrypt() {
        System.out.println(Md5Encrypt.md5("Yy@12345611111111111215"));

        System.out.println(Md5Encrypt.md5("hlw@36514526"));

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


    /**
     * 海鹚接口加密看这里
     */
    @Test
    public void testHCEncryptUtils() {
        String hisRequestParamsXML = "<Request>\n" +
                "    <serviceCode>SendMessageNotice</serviceCode>\n" +
                "    <partnerId>GXRMYY</partnerId>\n" +
                "    <timeStamp>2023-08-29 12:11:12</timeStamp>\n" +
                "    <password>68B1FF180C387FDEEC49672E6A2C6939</password>\n" +
                "    <cardNo>02722662</cardNo>\n" +
                "    <msgType>6</msgType>\n" +
                "    <msgContext>测试1</msgContext>\n" +
                "    <channel></channel>\n" +
                "    <msgUrl></msgUrl>\n" +
                "</Request>";

        JSONObject jsonObject = FangUtils.xml2Json(hisRequestParamsXML);
        Map params = JSONObject.parseObject(JSON.toJSONString(jsonObject), Map.class);
        String password = HCEncryptUtils.getMD5Value(params, "CDSDQRMYY2698");
        System.out.println("海鹚加密算法得到的password = " + password);//60CDBB07272E31591678B1BD6C188B7A
    }

    @Test
    public void testHCMoneyCovert() {

        String price = "525.51";

        System.out.println(FangUtils.coverMathFen(price));

        System.out.println(FangUtils.isDouble(price));
        System.out.println(FangUtils.isDouble("233"));

        System.out.println();
        System.out.println();


        String name = StringUtils.right(price, 1);
        System.out.println("name  " + name);
        System.out.println(StringUtils.leftPad(name, StringUtils.length(price), "*"));
    }

}
