package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.utils.common.DateTool;
import com.tsxy.utils.FangUtils;
import entity.PatientRequisitionVo;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.*;
import org.jdom2.JDOMException;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @Author Liu_df
 * @Date 2023/3/2 18:03
 */
public class ByteStringConversion {

    /**
     * byte 和 String 之前转换
     * 私自byte[]转会乱码
     */
    @Test
    public void testByteConversionString(){
        byte[] bytes = new byte[]{-98, -107, 110, -123, -120, 23, -57, -48, -70, -13, -56, -38, 96, -38, 80, 52, -94, -50, 85, -14, -123, 87, 20, -3, -126, 30, -85, -126, -41, 24, 63, 126, 2, -17, -21, 100, 61, -88, 35, 94, -97, -110, 45, -2, -51, -32, 33, -80, -124, -11, 66, -112, 34, -35, 9, -118, 113, -93, 7, 18, -13, -88, 16, 20};
        String string = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(string);
        byte[] bytes1 = string.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes1) {
            System.out.print(b + " ");
        }
    }

    @Test
    public void testStringConversionByte(){
        String str = "中国人";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (byte aByte : bytes) {
            System.out.print(aByte + " ");
        }
        System.out.println();
        String conversionStr = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(conversionStr);
    }


}
