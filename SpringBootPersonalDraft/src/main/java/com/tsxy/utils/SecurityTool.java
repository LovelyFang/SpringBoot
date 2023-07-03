package com.tsxy.utils;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @Author Liu_df
 * @Date 2023/7/3 10:59
 */
public class SecurityTool {



    private static Logger logger = LoggerFactory.getLogger(SecurityTool.class);

    private SecurityTool() {
    }

    /**
     * aes 加密操作
     * @param encryptData 需要加密的数据
     * @param encryptKey 加密的密钥
     * @return
     * @throws Exception
     */
    public static String aes_encrypt(String encryptData, String encryptKey) throws Exception {
        byte[] keyBytes = Arrays.copyOf(encryptKey.getBytes("ASCII"), 16);

        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cleartext = encryptData.getBytes("UTF-8");
        byte[] ciphertextBytes = cipher.doFinal(cleartext);

        return new String(Hex.encodeHex(ciphertextBytes));

    }

    /**
     * aes 解密操作
     * @param decryptData 需要解密的数据
     * @param decryptKey 解密的密钥
     * @return
     * @throws Exception
     */
    public static String aes_decrypt(String decryptData, String decryptKey) throws Exception {
        byte[] decodeHex = Hex.decodeHex(decryptData);
        byte[] keyBytes = Arrays.copyOf(decryptKey.getBytes("ASCII"), 16);

        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] ciphertextBytes = cipher.doFinal(decodeHex);

        return new String(ciphertextBytes, "UTF-8");

    }

    /**
     * 检查当前数据是否已经是加密数据
     * @param encryptData
     * @param encryptKey
     * @return true：未加密; false：已加密
     */
    public static boolean checkIsNeedEncrypt(String encryptData, String encryptKey) {
        try {
            aes_decrypt(encryptData, encryptKey);
        } catch (Exception e) {
            return true;
        }

        return false;
    }

}
