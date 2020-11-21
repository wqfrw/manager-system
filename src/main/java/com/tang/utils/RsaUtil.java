package com.tang.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 
 * @ClassName RsaUtil
 * @Description RSA加密工具
 * @author 芙蓉王
 * @Date Dec 28, 2019 11:30:45 AM
 * @version 1.0.0
 */
public class RsaUtil {

   /**
    * RSA最大加密明文大小
    */
   private static final int MAX_ENCRYPT_BLOCK = 117;

   /**
    * RSA最大解密密文大小
    */
   private static final int MAX_DECRYPT_BLOCK = 128;
   
   /**
    * 私钥
    */
   public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK0uC5TB0Gh1KGqxrpDWuv02zKdM/aXXFJvyG7xG1SboKyut17uYZjSOpq6Uc7cVHVYvV8Eg04rAA551sF3Zxq8xTqprhA3jR6t+90Uj2RNKxhPcijHmKbmLM/3uw6S3rSpCoXVZKO+42bY4LTnipTKG1phHCoWLO5TzlwtWO88dAgMBAAECgYAIHXM/ekGI04ycS00xzk4e1XWLlWkBX23AzPyNuwf79b8oIdJNIRwQ9GpNHfm6J4wjYL6M9zvScaL5xuwHyc+sjDON8tysDPZUNUsJ76g86yKM6nv93nOGLh8viX2VELZioDShPem5BfdV9v8D0P+AW0XKo3JZKL7M7del8VgLJQJBANvRgHpM3Q6ExiJlOMCedHKnUD48F06382myYJDEcNpBdxif2fwPCWAoZgQ7i3R6GNmkMo9Q+RN4iJ7yWl6ujmcCQQDJr1WWomZLr2AZhzFCHtA+8qmGDg23lru9hs++b1wFJWYW+pE7V9eIwv3Qqdcb8s1rqcblaE4bPDK0yw4Uz/vbAkBDcOx1GV39FVlgtiOKXVMm1hR1c6RG/4ML27Fq2QoyvEEY6fpMLiTXPq3GAKrRiqBTLeBhPhudEK8B7SMyx2VjAkAKBSQDb94VNIlZu1W/Kzi9Z+D0QA5+aIa7S89WbsHrn4gK7Df68spKiSWDEG0XfmCmULEJBL2crMj4In54mjyxAkEAp0d32yP8Su2yuwdRag/2C/OwftPvLAeWSfNRXGtPQfYtpBmKUllNGb/yVnRqnH/5FWV/28fRPEkBefwf6IGSnA==";
   
   /**
    * 公钥
    */
   public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtLguUwdBodShqsa6Q1rr9NsynTP2l1xSb8hu8RtUm6Csrrde7mGY0jqaulHO3FR1WL1fBINOKwAOedbBd2cavMU6qa4QN40erfvdFI9kTSsYT3Iox5im5izP97sOkt60qQqF1WSjvuNm2OC054qUyhtaYRwqFizuU85cLVjvPHQIDAQAB";

   /**
    * 获取密钥对
    *
    * @return 密钥对
    */
   public static KeyPair getKeyPair() throws RuntimeException {
       try {
           KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
           generator.initialize(1024);
           return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * 获取私钥
    *
    * @param privateKey 私钥字符串
    * @return
    */
   public static PrivateKey getPrivateKey() throws RuntimeException {
       try {
           KeyFactory keyFactory = KeyFactory.getInstance("RSA");
           byte[] decodedKey = Base64.getMimeDecoder().decode(PRIVATE_KEY.getBytes());
           PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
           return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * 获取公钥
    *
    * @param publicKey 公钥字符串
    * @return
    */
   public static PublicKey getPublicKey() throws RuntimeException {
       try {
           KeyFactory keyFactory = KeyFactory.getInstance("RSA");
           byte[] decodedKey = Base64.getMimeDecoder().decode(PUBLIC_KEY.getBytes());
           X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
           return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * RSA加密
    *
    * @param data      待加密数据
    * @param publicKey 公钥
    * @return
    */
   public static String encrypt(String data, PublicKey publicKey) throws RuntimeException {
       try {
           Cipher cipher = Cipher.getInstance("RSA");
           cipher.init(Cipher.ENCRYPT_MODE, publicKey);
           int inputLen = data.getBytes().length;
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int offset = 0;
           byte[] cache;
           int i = 0;
           // 对数据分段加密
           while (inputLen - offset > 0) {
               if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                   cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
               } else {
                   cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
               }
               out.write(cache, 0, cache.length);
               i++;
               offset = i * MAX_ENCRYPT_BLOCK;
           }
           byte[] encryptedData = out.toByteArray();
           out.close();
           // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
           // 加密后的字符串
           return new String(Base64.getMimeEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * RSA解密
    *
    * @param data       待解密数据
    * @param privateKey 私钥
    * @return
    */
   public static String decrypt(String data, PrivateKey privateKey) throws RuntimeException {
       try {
           Cipher cipher = Cipher.getInstance("RSA");
           cipher.init(Cipher.DECRYPT_MODE, privateKey);
           byte[] dataBytes = Base64.getMimeDecoder().decode(data);
           int inputLen = dataBytes.length;
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int offset = 0;
           byte[] cache;
           int i = 0;
           // 对数据分段解密
           while (inputLen - offset > 0) {
               if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                   cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
               } else {
                   cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
               }
               out.write(cache, 0, cache.length);
               i++;
               offset = i * MAX_DECRYPT_BLOCK;
           }
           byte[] decryptedData = out.toByteArray();
           out.close();
           // 解密后的内容
           return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * 签名
    *
    * @param data       待签名数据
    * @param privateKey 私钥
    * @return 签名
    */
   public static String sign(String data, PrivateKey privateKey) throws RuntimeException {
       try {
           byte[] keyBytes = privateKey.getEncoded();
           PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
           KeyFactory keyFactory = KeyFactory.getInstance("RSA");
           PrivateKey key = keyFactory.generatePrivate(keySpec);
           Signature signature = Signature.getInstance("MD5withRSA");
           signature.initSign(key);
           signature.update(data.getBytes());
           return new String(Base64.getMimeEncoder().encodeToString(signature.sign()));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
   }

   /**
    * 验签
    *
    * @param srcData   原始字符串
    * @param publicKey 公钥
    * @param sign      签名
    * @return 是否验签通过
    */
   public static boolean verify(String srcData, PublicKey publicKey, String sign) throws RuntimeException {
       try {
           byte[] keyBytes = publicKey.getEncoded();
           X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
           KeyFactory keyFactory = KeyFactory.getInstance("RSA");
           PublicKey key = keyFactory.generatePublic(keySpec);
           Signature signature = Signature.getInstance("MD5withRSA");
           signature.initVerify(key);
           signature.update(srcData.getBytes());
           return signature.verify(Base64.getMimeDecoder().decode(sign.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
       
   }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return 加密后的字节数组
     * @throws  
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws RuntimeException {
        try {
            byte[] singnStr = data.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = singnStr.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(singnStr, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(singnStr, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }




    public static void main(String[] args) {
       try {
           // 生成密钥对
           KeyPair keyPair = getKeyPair();
           String privateKey = new String(Base64.getMimeEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
           String publicKey = new String(Base64.getMimeEncoder().encodeToString(keyPair.getPublic().getEncoded()));
           System.out.println("私钥:" + privateKey);
           System.out.println("公钥:" + publicKey);
           // RSA加密
           String data = "待加密的文字内容";
           String encryptData = encrypt(data, getPublicKey());
           System.out.println("加密后内容:" + encryptData);
           // RSA解密
           String decryptData = decrypt(encryptData, getPrivateKey());
           System.out.println("解密后内容:" + decryptData);

           // RSA签名
           String sign = sign(data, getPrivateKey());
           // RSA验签
           boolean result = verify(data, getPublicKey(), sign);
           System.out.print("验签结果:" + result);
       } catch (Exception e) {
           e.printStackTrace();
           System.out.print("加解密异常");
       }
   }
}
