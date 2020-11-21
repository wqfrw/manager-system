package com.tang.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * @ClassName SHAUtil
 * @Description SHA加密工具类
 * @Author 芙蓉王
 * @Date 2020-01-28 09:36
 * @Version 1.0
 **/
public class SHAUtil {

    /**
     * 标准的SHA256加密工具类
     * 使用Apache工具类实现
     */
    public static String SHA256Str(String str) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
        String encdeStr = Hex.encodeHexString(hash);
        return encdeStr;
    }

    public static void main(String[] args) throws Exception {
        String s = SHA256Str("1412451");
        System.out.println(s);
    }
}
