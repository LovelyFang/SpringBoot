package com.tsxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.utils.common.DateTool;
import com.tsxy.entity.BaseMpVo;
import com.tsxy.methods.CompletableFutureMethod;
import com.tsxy.utils.FangUtils;
import com.tsxy.utils.FunctionMethodUtils;
import com.tsxy.utils.medical.MedicalInsuranceUtils;
import com.tsxy.utils.medical.RefreshTestZhyyEnvYbAccessTokenUtil;
import com.tsxy.utils.medical.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author Liu_df
 * @Date 2022/12/21 15:26
 */
public class FangUtilsTest {


    @Test
    public void testXml2JSON() throws IOException, JDOMException {

        String request = "<Response>" +
                "<data>" +
                "<dat>" +
                "<AEpisodeID>2100021</AEpisodeID>" +
                "<InstanceID>1843554||1^382</InstanceID>" +
                "<admDate>2022-12-07</admDate>" +
                "<admDoc>彭波</admDoc>" +
                "<admloc>感染性疾病科门诊</admloc>" +
                "<code>1</code>" +
                "<docID>382</docID>" +
                "<message>查询成功</message>" +
                "</dat>" +
                "<dat>" +
                "<AEpisodeID>2117431</AEpisodeID>" +
                "<InstanceID>1852408||1^382</InstanceID>" +
                "<admDate>2022-12-09</admDate>" +
                "<admDoc>温伶俐</admDoc>" +
                "<admloc>全科医学科门诊</admloc>" +
                "<code>1</code>" +
                "<docID>382</docID>" +
                "<message>查询成功</message>" +
                "</dat>" +
                "</data>" +
                "</Response>";
        /*
        {
            "Response": {
                "data": [{
                        "dat": [{
                                "code": "1",
                                "InstanceID": "1843554||1^382",
                                "docID": "382",
                                "AEpisodeID": "2100021",
                                "admDoc": "彭波",
                                "admDate": "2022-12-07",
                                "admloc": "感染性疾病科门诊",
                                "message": "查询成功"
                            }, {
                                "code": "1",
                                "InstanceID": "1852408||1^382",
                                "docID": "382",
                                "AEpisodeID": "2117431",
                                "admDoc": "温伶俐",
                                "admDate": "2022-12-09",
                                "admloc": "全科医学科门诊",
                                "message": "查询成功"
                            }
                        ]
                    }
                ]
            }
        }
        {
            "Response": {
                "data": [{
                        "dat": [{
                                "code": "1",
                                "InstanceID": "1843554||1^382",
                                "docID": "382",
                                "AEpisodeID": "2100021",
                                "admDoc": "彭波",
                                "admDate": "2022-12-07",
                                "admloc": "感染性疾病科门诊",
                                "message": "查询成功"
                            }
                        ]
                    }
                ]
            }
        }
         */
        JSONObject jsonObject = FangUtils.xml2JsonOther(request, "dat");
        System.out.println(jsonObject.toString());  // {"Response":{"resultCode":"-1","resultMessage":"生成作废发票记录失败,ORA-00001: 违反唯一约束条件"}}
    }

    /**
     * xml2Json 方法
     */
    @Test
    public void testXMLParse() throws IOException, JDOMException {
        String responseResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/'>\n" +
                "  <SOAP-ENV:Body><HIPMessageServerResponse xmlns=\"http://www.dhcc.com.cn\"><HIPMessageServerResult>" +
                "<![CDATA[<Response><ResultCode>0</ResultCode><ResultMsg>获取缴费记录成功</ResultMsg><RecordCount>1</RecordCount>" +
                "<RecordList><Record><OrderNo></OrderNo><InvoiceNo>3001109</InvoiceNo><InvDate>2023-03-06</InvDate>" +
                "<InvTime>14:42:38</InvTime><TotalAmt>236.21</TotalAmt><InsuShareAmt>236.21</InsuShareAmt>" +
                "<PatShareAmt>0</PatShareAmt><PayModeInfo>医保账户支付:173.06,医保基金支付:63.15</PayModeInfo>" +
                "<PrintFlag>N</PrintFlag><PrintNum>0</PrintNum><PrtInvNo></PrtInvNo><AdmDate>2023-03-06</AdmDate>" +
                "<AdmTime>14:42:09</AdmTime><AdmDept>便民门诊</AdmDept><AdmDoctor>吕丽坚</AdmDoctor>" +
                "<IUDPictureUrl>" +
                "<![CDATA[45060122^0185821908^07892e^https://www.chinaebill.cn/eips-wxapp-service/d?t=504&a=31PZV5IKDl&" +
                "d=45060122_0185821908_07892e_236.21_20230306&s=1FE6A1stmu611Dl_587F4EA2E3^1118743^I^]]]]>" +
                "<![CDATA[></IUDPictureUrl></Record></RecordList></Response>]]></HIPMessageServerResult>" +
                "</HIPMessageServerResponse></SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
        int i = responseResult.indexOf("<HIPMessageServerResult>");
        responseResult = responseResult.substring(i + "<HIPMessageServerResult>".length());
        int j = responseResult.indexOf("</HIPMessageServerResult>");
        responseResult = responseResult.substring(0, j);
        responseResult = responseResult.replace("<![CDATA[>","");
        responseResult = responseResult.replace("<![CDATA[","");
        responseResult = responseResult.replace("]]]]>","");
        responseResult = responseResult.replace("]]>","");
        responseResult = responseResult.replace("&", "&amp;");
        JSONObject json = FangUtils.xml2Json(responseResult);
        System.out.println(json.toJSONString());
    }

    /**
     * 医保token
     */
    @Test
    public void refreshMedicalInsuranceToken() throws Exception {
        Map<String, Object> params = new HashMap<>();
        String timeStamp = System.currentTimeMillis() + "";
        params.put("platformId", "2155");
        params.put("hisId", "2155");
        params.put("platformSource", "3");
        params.put("subSource", "1");//
        params.put("timestamp", timeStamp);
        params.put("ver", "1");
        System.out.println("params："+ JSON.toJSONString(params));
        String sign = RefreshTestZhyyEnvYbAccessTokenUtil.getHaiciSign(params, "xG1h@C(kc0MV^Res#vYiFKiw%cHIz&wn.oW$3eSEJ");
        System.out.println("sign ： " + sign);
    }


    /**
     * 根据日期获取年龄
     */
    @Test
    public void testGetAge() throws ParseException {

//        String patIdNo = "360622201701043235";
        String patIdNo = "2017-04-09";
        LocalDate now = LocalDate.now();
        LocalDate beforeDay = now.minusDays(1);
        System.out.println(now);
        System.out.println(beforeDay);

//        System.out.println(FangUtils.getAgeByBirth(patIdNo));

//        String respTotalAmtYuan = "10.78";
//        String selfAmtYuan = "0";
//        String medInsAmtYuan = "10.58";
//        double funPayYuan = Double.parseDouble(respTotalAmtYuan) - Double.parseDouble(selfAmtYuan);
//        System.out.println(funPayYuan);
//        funPayYuan -= Double.parseDouble(medInsAmtYuan);  // 医保基金支付  国家给你的钱
//        System.out.println(funPayYuan);

    }


    @Test
    public void testCustomFunctionMethod() {
        CompletableFutureMethod me = new CompletableFutureMethod();
        FunctionMethodUtils functionMethodUtils = new FunctionMethodUtils();
        long startTime = System.nanoTime();
        List<String> result = new ArrayList<>();
        List<String> getInspectList1 = null;
        try {
            getInspectList1 = me.GetInspectList1();
        } catch (Exception e) {
            System.out.println("抓到异常! 不抛出");
        }
        me.obtainResult(result, getInspectList1);
        List<String> getInspectList2 = me.GetInspectList2();
        me.obtainResult(result, getInspectList2);
        long endTime = System.nanoTime();
        System.out.printf("运行结束耗时:%s 微秒", (endTime - startTime)/1000000);
        System.out.println();
        System.out.println("----结果: " + result);

        result.clear();
        startTime = System.nanoTime();
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                List<String> getInspectList11 = me.GetInspectList1();
                me.obtainResult(result, getInspectList11);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            List<String> getInspectList22 = me.GetInspectList2();
            me.obtainResult(result, getInspectList22);
        });
        try {
            future1.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("捕获异常");
        }
        future2.join();

        endTime = System.nanoTime();
        System.out.printf("运行结束耗时:%s 微秒", (endTime - startTime)/1000000);
        System.out.println();
        System.out.println("结果: " + result);



        result.clear();
        startTime = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            if (i%2==0) {
                functionMethodUtils.executorInspect(me::GetInspectList1, me::obtainResult, result);
            }else {
                functionMethodUtils.executorInspect(me::GetInspectList2, me::obtainResult, result);
            }
        }
//        Future<?> future111 = executorInspect(me::GetInspectList1, me::obtainResult, result);
//        Future<?> future222 = executorInspect(me::GetInspectList2, me::obtainResult, result);
//        try {
//            future111.get(2, TimeUnit.SECONDS);
//            future222.get(2, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        endTime = System.nanoTime();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("===================运行结束耗时:%s 微秒", (endTime - startTime)/1000000);
        System.out.println();
        System.out.println("结果: " + JSON.toJSONString(result));

    }


}
