package com.tang;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 
 * @ClassName DruidPasswordTest
 * @Description 阿里巴巴数据库密码测试类
 * @author 芙蓉王
 * @Date Mar 2, 2020 6:14:42 PM
 * @version 1.0.0
 */
public class DruidPasswordTest {
    
    public static void main(String[] args) throws Exception {
        String secret = encrypt("123456");
        String password = decrypt(secret);
        System.out.println("加密密码:" + secret);
        System.out.println("密码明文:" + password);
    }
    
    /**
     * 
     * @Description 密码解密
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String password) throws Exception {
        return ConfigTools.decrypt(ConfigTools.DEFAULT_PUBLIC_KEY_STRING,password);
    }
    
    /**
     * 
     * @Description 密码加密
     * @param inputStr
     * @return
     * @throws Exception
     */
    public static String encrypt(String inputStr) throws Exception{
        return ConfigTools.encrypt(inputStr);
    }
}
