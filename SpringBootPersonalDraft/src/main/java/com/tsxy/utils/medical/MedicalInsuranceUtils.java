package com.tsxy.utils.medical;

/**
 * @Author Liu_df
 * @Date 2023/5/9 17:34
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.Hex;

import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

/**
 * 医保方法
 */
public class MedicalInsuranceUtils {

    private final static String version = "2.0.1";
    private final static String encType = "SM4";
    private final static String signType = "SM2";

    public static JSONObject encryptMsg(String chnlId, String sm4key, String prvkey, JSONObject body) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",chnlId);
        jsonObject.put("encType",encType);
        jsonObject.put("data",body);
        jsonObject.put("signType",signType);
//        jsonObject.put("timestamp", "1683531146396");
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("version",version);
        //加密后的报文
        String encData = sm4Encrypt(chnlId,sm4key, body.toJSONString());
        //签名
        String signData = signature(jsonObject.toJSONString(), sm4key, prvkey);
        jsonObject.fluentRemove("data");
        jsonObject.put("encData",encData);
        jsonObject.put("signData",signData);
        return jsonObject;
    }

    /**
     * sm4加密
     * @param chnlId 渠道id
     * @param sm4key 渠道sm4密钥
     * @param message 待加密报文
     * @return 加密后的报文内容 String
     * @throws Exception
     */
    public static String sm4Encrypt(String chnlId,String sm4key,String message) throws Exception {
        //用appId加密appSecret获取新秘钥
        byte[] appSecretEncData = EasyGmUtils.sm4Encrypt(chnlId.substring(0, 16).getBytes("UTF-8"), sm4key.getBytes("UTF-8"));
        //新秘钥串
        byte[] secKey = Hex.toHexString(appSecretEncData).toUpperCase().substring(0, 16).getBytes("UTF-8");
        //加密数据
        return Hex.toHexString(EasyGmUtils.sm4Encrypt(secKey, message.getBytes("UTF-8"))).toUpperCase();
    }

    /**
     * sm2签名
     * @param message 未加密报文
     * @param sm4key 渠道sm4密钥
     * @param prvKey 渠道私钥
     * @return 签名串 String
     * @throws Exception
     */
    public static String signature(String message,String sm4key,String prvKey){
        byte[] messageByte;
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            removeEmpty(jsonObject);
            messageByte = SignUtil.getSignText(jsonObject, sm4key).getBytes("UTF-8");
        }catch (Exception e){
            messageByte = message.getBytes();
        }
        byte[] chnlSecretByte = sm4key.getBytes();
        byte[] prvkey = Base64.getDecoder().decode(prvKey);
        return Base64.getEncoder().encodeToString(EasyGmUtils.signSm3WithSm2(messageByte, chnlSecretByte, prvkey));
    }

    /**
     * 移除json中空值的键值对
     * @param jsonObject
     */
    private static void removeEmpty(JSONObject jsonObject){
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                // 数组长度为0时将其处理,防止Gson转换异常
                if (jsonArray.size() == 0) {
                    it.remove();
                } else {
                    for (Object o : jsonArray) {
                        JSONObject asJsonObject = (JSONObject) o;
                        removeEmpty(asJsonObject);
                    }
                }
            }
            if (value instanceof JSONObject) {
                JSONObject asJsonObject = (JSONObject) value;
                removeEmpty(asJsonObject);
            }
            if (value == null){
                it.remove();
            }
            if (value instanceof String && StringUtil.isEmpty(value)){
                it.remove();
            }
        }
    }

}
