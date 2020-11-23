package com.tang.base.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 
 * @ClassName SecurityPasswordEncoder
 * @Description 自定义密码校验
 * @author 芙蓉王
 * @Date Mar 13, 2020 11:26:41 AM
 * @version 1.0.0
 */
public class SecurityPasswordEncoder extends BCryptPasswordEncoder {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityPasswordEncoder.class);
    
    /**
     * 重写加密比较器(使用密码+盐 )
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String password = rawPassword.toString();
        return password.equals(encodedPassword);
    }
    
}
