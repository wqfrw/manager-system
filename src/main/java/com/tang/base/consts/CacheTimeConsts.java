package com.tang.base.consts;

/**
 * 
 * @ClassName CacheTimeConsts
 * @Description 缓存时间常量接口
 * @author 芙蓉王
 * @Date Mar 2, 2020 9:54:26 PM
 * @version 1.0.0
 */
public interface CacheTimeConsts {

    /**
     * JWT有效期为十分钟
     */
    long JWT_EXPIRED_TIME = 7200*1000;
//    public static final long JWT_EXPIRED_TIME = 600 * 1000;

    /**
     * 旧票据存在时间，单位：秒
     */
    long OLD_JWT_TICKET_EXPIRED_TIME = 300;
    
    /**
     * 用户登录key过期时间
     */
    long ADMIN_USER_LOGIN_TOKEN_EXPIRED_TIME = 7200;

}
