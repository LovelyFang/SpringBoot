package com.tsxy.methods;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2024/3/8 16:33
 */
@Component
public class GateWayUtils {

    private static final Logger logger = LoggerFactory.getLogger(GateWayUtils.class);

    /**
     * 执行一个带参数的HTTP POST请求，返回请求响应的JSON字符串
     *
     * @param url 请求的URL地址
     * @param map 请求的map参数
     * @return 返回请求响应的JSON字符串
     */
    public static String doPost(String url, Map<String, Object> map) {
        // 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        // 创建POST方法的实例
        PostMethod method = new PostMethod(url);
        // 一些请求配置参数
        HttpClientParams httpClientParams = new HttpClientParams();
        httpClientParams.setSoTimeout(30000);   // 设置读数据超时时间(单位毫秒)
        httpClientParams.setConnectionManagerTimeout(10000); // 设置连接超时时间(单位毫秒)
        httpClientParams.setContentCharset("UTF-8");
        method.setParams(httpClientParams);
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // 创建basicNameValue
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                nameValuePairs.add(new NameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        // 填入各个表单域的值
        NameValuePair[] param = nameValuePairs.toArray(new NameValuePair[0]);
        // 将表单的值放入postMethod中
        method.addParameters(param);
        try {
            // 执行请求
            httpClient.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                return StreamUtils.copyToString(method.getResponseBodyAsStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.error("address:{}, 执行HTTP Post请求时发生异常！", url, e);
        } finally {
            method.releaseConnection();
        }
        return "";
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url     请求的URL地址
     * @param contentType contentType
     * @param reqStr  请求的查询参数,可以为 null
     * @param charset 字符集
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, String reqStr, String contentType, String charset) {
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
            managerParams.setConnectionTimeout(30000); // 设置连接超时时间(单位毫秒)
            managerParams.setSoTimeout(30000); // 设置读数据超时时间(单位毫秒)
            method.setRequestEntity(new StringRequestEntity(reqStr, contentType, "utf-8"));
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                return StreamUtils.copyToString(method.getResponseBodyAsStream(), Charset.forName(charset));
            }
        }catch (IOException e) {
            logger.error("address:{}, 执行HTTP Post请求时发生异常！", url, e);
        } finally {
            method.releaseConnection();
        }
        return "";
    }

}
