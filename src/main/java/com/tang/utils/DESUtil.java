package com.tang.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author 芙蓉王
 * @version 1.0.0
 * @ClassName DESUtil
 * @Description DES加密工具类
 * @Date Jan 15, 2020 9:50:23 PM
 */
public class DESUtil {

    /**
     * @param value
     * @param key
     * @return
     * @throws Exception
     * @Description DES加密工具类
     */
    public static String DESEncrypt(String value, String key) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        Cipher localCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
        byte[] result = localCipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String str = Base64.getEncoder().encodeToString(result);// 此处使用BASE64做转码
        return URLEncoder.encode(str, "UTF-8");//URL加密
    }

    /**
     * DES/CBC/PKCS5Padding 加密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] DCPencrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * DES算法加密
     *
     * @return 加密后的密文
     * @throws Exception 声明Exception异常
     */
    public static String encrypt(String str, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes())).replaceAll("\\s*", "");
    }

    /**
     * DES算法解密
     *
     * @param cipherText 需要解密的密文
     * @return 解密后的明文
     * @throws Exception 声明Exception异常
     */
    public static String decrypt(String cipherText, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

    /**
     * 加密<br/>
     *
     * @param data 被解码的数据(注意编码转换)
     * @param key  key
     * @param iv   向量(必须为8byte)
     * @return
     * @throws GeneralSecurityException
     */
    public static String DESsdeEncrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey sec = keyFactory.generateSecret(new DESedeKeySpec(key));
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        IvParameterSpec ivParameters = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, sec, ivParameters);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    /**
     * @param reqStr
     * @param
     * @return
     * @throws Exception
     * @Description 欢乐棋牌专用
     */
    public static String DESsdeEncrypt(String reqStr, String secret) throws Exception {
        byte[] md5Key = new byte[24];
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(secret.getBytes(StandardCharsets.UTF_8), 0, secret.length());
        byte[] md5 = messageDigest.digest();
        System.arraycopy(md5, 0, md5Key, 0, 16);
        System.arraycopy(md5, 0, md5Key, 16, 8);

        SecretKey sec = new SecretKeySpec(md5Key, "DESede");
        Cipher ecipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, sec);
        byte[] data = reqStr.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedArray = ecipher.doFinal(data);
        return Base64.getEncoder().encodeToString(encryptedArray);
    }

    /**
     * 结果转为16进制
     * DES加密算法
     */
    public static String encryptHexadecimal(String plainText, String keys) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(keys.getBytes(StandardCharsets.US_ASCII));
        SecretKeySpec key = new SecretKeySpec(keys.getBytes(StandardCharsets.US_ASCII), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] b = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        //将二进制转换成16进制
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String pwd = "Aa@123456";
        String key = "helloWord";
        try {
            String encrypt = encrypt(pwd, key);
            System.out.println("加密后密码为" + encrypt);
            String decrypt = decrypt(encrypt, key);
            System.out.println("解密后密码为" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
