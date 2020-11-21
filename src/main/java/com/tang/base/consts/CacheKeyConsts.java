package com.tang.base.consts;

/**
 * 
 * @ClassName CacheConsts
 * @Description 缓存KEY常量接口
 * @author 芙蓉王
 * @Date Mar 2, 2020 9:52:53 PM
 * @version 1.0.0
 */
public interface CacheKeyConsts {

    /**
     * 签发人key
     */
    String JWT_ISSUER="T-Token";
    
    /**
     * 用户登录信息key
     */
    String ADMIN_USER_LOGIN_TOKEN_KEY = "ADMIN:USER:LOGIN:TOKEN:";
    
    /**
     * 登录jwt票据key
     */
    String ADMIN_USER_LOGIN_JWT_TICKET_KEY="ADMIN:USER:LOGIN:JWT:TICKET:";
    
    /**
     * 用户登录的旧jwt票据key
     */
    String ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY="ADMIN:USER:LOGIN:OLD:JWT:TICKET:";
}
