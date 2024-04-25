package com.tsxy.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 一些网络相关的工具方法.
 * @Author Liu_df
 * @Date 2024/4/9 10:11
 */
public class NetUtils {

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    public static final String ANYHOST = "0.0.0.0";

    public static final String LOCALHOST = "127.0.0.1";

    /**
     * 获取本地ip地址
     * @return 获取本地ip地址
     */
    public static String getLocalIpAddress() {

        InetAddress localAddress = getLocalAddress();

        return localAddress == null ? LOCALHOST : localAddress.getHostAddress();
    }

    /**
     * 遍历本地网卡，返回第一个合理的IP。
     * @return 本地网卡IP
     */
    private static InetAddress getLocalAddress() {

        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return localAddress;
    }



    /** 判断是否有效ip **/
    private static boolean isValidAddress(InetAddress address) {
        if (address == null
                || address.isLoopbackAddress())
            return false;

        String name = address.getHostAddress();

        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

}
