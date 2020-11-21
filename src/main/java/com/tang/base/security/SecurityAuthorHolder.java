package com.tang.base.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * @ClassName SecurityAuthorHolder
 * @Description 
 * @author 芙蓉王
 * @Date Mar 7, 2020 12:41:09 PM
 * @version 1.0.0
 */
public class SecurityAuthorHolder {
    
    /**
     * 
     * @Description 获取用户信息
     * @return
     */
    public static SecurityUser getSecurityUser() {
        return  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
