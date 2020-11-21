package com.tang.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;


/**
 * @author 芙蓉王
 * @version 1.0.0
 * @ClassName AESUtil
 * @Description AES加密工具类
 * @Date Jan 15, 2020 9:22:41 PM
 */
public class AESUtil {
    public static boolean initialized = false;

    /**
     * @param value
     * @param key
     * @return
     * @throws Exception
     * @Description 加密
     */
    public static String AESEncrypt(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        String base64 = Base64.getEncoder().encodeToString(encrypted);
        return URLEncoder.encode(base64, "UTF-8");
    }

    /**
     * @param value
     * @param key
     * @return
     * @throws Exception
     * @Description 加密
     */
    public static String AESEncryptPKCS7(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] ciphertext = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(ciphertext);
    }

    /**
     * @param value
     * @param key
     * @return
     * @throws Exception
     * @Description 解密
     */
    public static String AESDecryptPKCS7(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] ciphertext = cipher.doFinal(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)));
        return new String(ciphertext, StandardCharsets.UTF_8);
    }

    /**
     * 通过byte[]类型的密钥 解密String类型的密文
     *
     * @param content
     * @param
     * @return
     */
    public static String RRFZDecrypt(String content, String aesKey) {
        try {
            byte[] keyByte = aesKey.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "AES"));

            if (content == null || content.length() < 2) {
                return new String(cipher.doFinal(new byte[0]), StandardCharsets.UTF_8);
            }

            content = content.toLowerCase();
            int l = content.length() / 2;
            byte[] result = new byte[l];
            for (int i = 0; i < l; ++i) {
                String tmp = content.substring(2 * i, 2 * i + 2);
                result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
            }

            return new String(cipher.doFinal(result), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过byte[]类型的密钥加密String
     *
     * @param content
     * @param
     * @return 16进制密文字符串
     */
    public static String RRFZEncrypt(String content, String aesKey) {
        try {
            byte[] keyByte = aesKey.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyByte, "AES"));
            byte[] data = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

            // 整数转成十六进制表示
            StringBuilder builder = new StringBuilder(data.length * 2);
            for (int n = 0; n < data.length; n++) {
                String tmp = Integer.toHexString(data[n] & 0XFF);
                if (tmp.length() == 1) builder.append("0");
                builder.append(tmp);
            }
            return builder.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES256解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String aesDecrypt(String content, String key) {
        initialize();
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            return new String(cipher.doFinal(hexStringToByteArray(content)), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return null;
    }

    /**
     * AES256加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String aesEncrypt(String content, String key) {
        initialize();
        byte[] result = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return bytesToHex(result);
    }

    /**
     * AES 加密方法
     *
     * @param encData 需要加密的参数，用于加密下单请求的参数
     * @param aesKey  AES Key，可以从“商户后台 - 账户信息”获取
     * @param aesIv   AES IV，可以从“商户后台 - 账户信息”获取
     * @return 加密后的内容
     * @throws Exception
     */
    public static String encryptWithIv(String encData, String aesKey, String aesIv)
            throws Exception {

        if (aesKey == null) {
            return null;
        }
        if (aesKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = aesKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(aesIv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 加密字节数据
     *
     * @param content
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密字节数组
     **/
    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建密钥
     **/
    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }


        data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data;
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new IvParameterSpec(data);
    }

    /**
     * 通过String类型的密钥加密String
     *
     * @param content
     * @param key
     * @return 16进制密文字符串
     */
    public static String encrypt(String content, String key) {
        byte[] data = null;
        try {
            data = content.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, new SecretKeySpec(hex2byte(key), "AES").getEncoded());
        return bytesToHex(data);
    }

    /**
     * 将hex字符串转换成字节数组
     **/
    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }


    public static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }
}
