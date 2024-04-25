package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.utils.exceptions.BizException;
import com.tsxy.utils.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 智慧医院用户数据加密字段（解密不对，应该不是这个）
     */
    @Test
    public void zhYYSecurityTool() {
        String encryptKey = "C3dTTCok&AA24_2P";
        String encryptedData = "qSO9s3pWhoz2RJ0JbPsWKw==";
        try {
            byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
            Security.addProvider(new BouncyCastleProvider());
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptByte = cipher.doFinal(Base64.decodeBase64(encryptedData));
            String s = new String(encryptByte);
            System.out.println(s);
            System.out.println(ALLATORIxDEMO(s));

        } catch (Exception e) {
            System.out.println("error====>" + e);
        }
    }

    public static String ALLATORIxDEMO(String a) {
        int var10000 = (2 ^ 5) << 4 ^ 2 << 2 ^ 3;
        int var10001 = 3 << 3 ^ 2 ^ 5;
        int var10002 = 2 << 3 ^ 3;
        int var10003 = a.length();
        char[] var10004 = new char[var10003];
        boolean var10006 = true;
        int var5 = var10003 - 1;
        var10003 = var10002;
        int var3;
        var10002 = var3 = var5;
        char[] var1 = var10004;
        int var4 = var10003;
        var10000 = var10002;

        for(int var2 = var10001; var10000 >= 0; var10000 = var3) {
            var10001 = var3;
            char var6 = a.charAt(var3);
            --var3;
            var1[var10001] = (char)(var6 ^ var2);
            if (var3 < 0) {
                break;
            }

            var10002 = var3--;
            var1[var10002] = (char)(a.charAt(var10002) ^ var4);
        }

        return new String(var1);
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
                "\t<serviceCode>SendMessageNotice</serviceCode>\n" +
                "\t<partnerId>gdszyzhyy</partnerId>\n" +
                "\t<msgType>3</msgType>\n" +
                "\t<channel>WX</channel>\n" +
                "\t<doctorName>医生</doctorName>\n" +
                "\t<cardNo>90399637</cardNo>\n" +
                "\t<patName>cgq</patName>\n" +
                "\t<params>{\"url1\":\"https://mp.med.gzhc365.com/views/survey/index.html?returnRandomParam=1639390259339\",\"url2\":\"#/survey/answer?hisId=2449\",\"url3\":\"id=3759324939677990912\",\"url4\":\"orderId=4925912360548401162\",\"url5\":\"rules=\",\"url6\":\"_k=q2a88e\"}</params>\n" +
                "    <timeStamp>2024-07-15 17:00:09</timeStamp>\n" +
                "\t<password>CA3E5ED449985450959AAB564D55EAAC</password>\n" +
                "</Request>";

        JSONObject jsonObject = FangUtils.xml2Json(hisRequestParamsXML);
        Map params = JSONObject.parseObject(JSON.toJSONString(jsonObject), Map.class);
        String password = HCEncryptUtils.getMD5Value(params, "gdszyzhyy2020");
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

        String text = "557-枳壳(麸炒枳壳).jpg";

        String regex = "^\\d+-[[\\u3400-\\u9FBF]|[()\\[\\]{}（）]]+\\.[a-zA-Z]+";
        Pattern pattern = Pattern.compile(regex);
        System.out.println();
        System.out.println(pattern.matcher(text).matches());

        regex = "\\d+|[[\\u3400-\\u9FBF]|[()\\[\\]{}（）]]+";
        pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }


        System.out.println(NetUtils.getLocalIpAddress());


    }
}
