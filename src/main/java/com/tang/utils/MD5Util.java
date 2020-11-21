package com.tang.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MD5Utils
 * @Description MD5工具类
 * @author 芙蓉王
 * @Date 2018年9月12日 上午11:53:04
 * @version 1.0.0
 */
public class MD5Util {
	
	
    /**
     * @Description 32位MD5加密---大写
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5toUpCase_32Bit(String str) throws RuntimeException{
        String result = md5(str);
        return result.toUpperCase();
    }

    /**
     * @Description 16位MD5加密---大写
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5toUpCase_16Bit(String str) throws RuntimeException {
        String result = md5(str);
        return result.substring(8, 24).toUpperCase();
    }

    /**
     * @Description 32位MD5加密---小写
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5toLowerCase_32Bit(String str) throws RuntimeException {
    	String result = md5(str);
        return result.toLowerCase();
    }
    
    /**
     * @Description 16位MD5加密---大写
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5toLowerCase_16Bit(String str) throws RuntimeException {
        String result = md5(str);
        return result.substring(8, 24).toLowerCase();
    }
    
    /**
     * 
     * @Description MD5加密串
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String md5(String str) throws RuntimeException {
    	try {
    	    MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            // 字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()); 
        }
    }

    /**
     * MD5算法加密
     * @param plainText 需要加密的明文
     * @param encoding 编码类型
     * @return 密文字串
     */
    public static String md5toUpCase_32Bit(String plainText,String encoding) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = plainText.getBytes(encoding);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static void main(String[] args) throws Exception {
		String refer = "localhost:3000";
		
		String result = md5toUpCase_16Bit(refer);
		System.err.println(result);
	}
}
