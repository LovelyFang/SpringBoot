package com.tsxy.riskcontrol.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:27
 */
public class HashUtil {

    private static final String MD5		= "MD5";
    private static final char DIGITS[]	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);

        for (int i = 0; i < data.length; i++) {
            sb.append(DIGITS[(data[i] & 0xf0) >>> 4]);
            sb.append(DIGITS[data[i] & 0x0f]);
        }

        Integer.toHexString(1);

        return sb.toString();
    }


    public static String md5(String source) {

        MessageDigest digest = null;

        if(source != null) {
            try {
                digest = MessageDigest.getInstance(MD5);
            } catch (NoSuchAlgorithmException e) {
                // ignore
            }

            if(digest != null) {
                digest.reset();
                digest.update(source.getBytes());

                return toHexString(digest.digest());
            } else {
                return source;
            }
        }

        return null;
    }
}
