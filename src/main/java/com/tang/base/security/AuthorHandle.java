package com.tang.base.security;

import com.alibaba.fastjson.JSONObject;
import com.tang.base.consts.CacheKeyConsts;
import com.tang.base.consts.CacheTimeConsts;
import com.tang.base.exception.ExpireException;
import com.tang.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

/**
 * @author 芙蓉王
 * @version 1.0.0
 * @ClassName AuthorHandle
 * @Description 权限操作类
 * @Date Mar 3, 2020 3:37:24 PM
 */
public class AuthorHandle {

    private static final Logger log = LoggerFactory.getLogger(AuthorHandle.class);

    /**
     * @param user
     * @return
     * @throws Exception
     * @Description 处理登录成功业务
     */
    public static void loginSuccessHandle(SecurityUser user) throws RuntimeException {
        //判断用户是否已登录,如果已登录则踢线
        String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + user.getLoginName());
        if (RedisUtil.hasKey(loginKey)) {
            //踢线
            SecurityUser loginUser = RedisUtil.get(loginKey, SecurityUser.class);
            if (loginUser != null) {
                //获取用户的有效令牌
                String token = loginUser.getToken();
                //删除用户的票据
                String jwt = getJwtTicket(token);
                //生成用户的登录票据key
                String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(jwt));
                //生成用户的旧票据key
                String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(jwt));
                //删除票据
                if (RedisUtil.hasKey(ticketKey)) RedisUtil.delete(ticketKey);
                //删除旧票据
                if (RedisUtil.hasKey(oldTicketKey)) RedisUtil.delete(oldTicketKey);
            }
        }
        addChacheUserToRedis(user);
    }

    /**
     * @param request
     * @throws Exception
     * @Description 退出登录操作缓存
     */
    public static void destoryUserFromRedis(HttpServletRequest request, HttpServletResponse response) throws RuntimeException {
        //获取用户信息
        SecurityUser user = getCacheUser(request);
        //生成用户登录缓存key
        String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + user.getLoginName());
        //生成用户的登录票据key
        String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(user.getToken())));
        //旧票据key
        String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(user.getToken())));
        //清空缓存信息
        if (RedisUtil.hasKey(loginKey)) RedisUtil.delete(loginKey);
        //清空缓存票据
        if (RedisUtil.hasKey(ticketKey)) RedisUtil.delete(ticketKey);
        //清空缓存旧票据
        if (RedisUtil.hasKey(oldTicketKey)) RedisUtil.delete(oldTicketKey);
        //初始化session信息
        request.getSession().invalidate();
        //初始化cookies信息
        CookiesUtil.destoryCookie(request, response);
    }


    /**
     * @param user
     * @throws Exception
     * @Description 添加用户信息到缓存中
     */
    private static void addChacheUserToRedis(SecurityUser user) throws RuntimeException {
        //组装jwt签名类
        JWTClaims jwtClaims = new JWTClaims(user.getId(), user.getLoginName());
        //获取token
        String authorization = getAuthorizationToken(generatorJwtToken(jwtClaims)).replaceAll("\r|\n", "");
        user.setToken(authorization);
        //存入用户信息到缓存中
        String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + user.getLoginName());
        //写入用户信息入缓存
        RedisUtil.set(loginKey, user, CacheTimeConsts.ADMIN_USER_LOGIN_TOKEN_EXPIRED_TIME);
        //获取jwt签名票据
        String jwt = getJwtTicket(user.getToken());
        //票据缓存key
        String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(jwt));
        RedisUtil.set(ticketKey, loginKey, CacheTimeConsts.ADMIN_USER_LOGIN_TOKEN_EXPIRED_TIME);
    }

    /**
     * @param request
     * @return
     * @throws Exception
     * @Description 获取缓存信息
     */
    public static SecurityUser getCacheUser(HttpServletRequest request) throws RuntimeException {
        String authorizationToken = request.getHeader(CacheKeyConsts.JWT_ISSUER);
        return getCacheUserFromRedis(authorizationToken);
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     * @Description 获取用户缓存信息
     */
    public static SecurityUser getCacheUser(String authorization) throws RuntimeException {
        return getCacheUserFromRedis(authorization);
    }

    /**
     * @param authorization
     * @return
     * @throws Exception
     * @Description 从缓存获取用户信息
     */
    private static SecurityUser getCacheUserFromRedis(String authorization) throws RuntimeException {
        try {
            //判断提交过来的令牌票据是否存在
            String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(authorization)));
            if (!RedisUtil.hasKey(ticketKey)) {
                //校验旧票据
                String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(authorization)));
                if (!RedisUtil.hasKey(oldTicketKey)) {
                    throw new ExpireException("令牌已过期,请重新登录");
                }
            }

            //获取JWT签名报文
            Claims claims = AuthorHandle.validAuthorizationToken(authorization);
            //获取加密报文
            JWTClaims jwtClaims = JSONObject.parseObject(claims.getSubject(), JWTClaims.class);
            //获取用户登录key
            String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + jwtClaims.getUsername());
            if (StringUtils.isBlank(loginKey)) {
                throw new RuntimeException("从缓存中查询用户信息失败");
            }
            //获取用户缓存信息
            SecurityUser user = RedisUtil.get(loginKey, SecurityUser.class);
            if (user == null) throw new ExpireException("令牌已过期,请重新登录");
            return user;
        } catch (JwtException e) {
            //刷新JWT签名票据
            return freshJwtTicket(authorization);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从缓存中获取用户信息异常,Token令牌:{}", authorization);
            throw new ExpireException(e.getMessage());
        }
    }


    /**
     * @param authorizationToken
     * @return
     * @throws Exception
     * @Description 获取JWT签名票据
     */
    public static String getJwtTicket(String authorizationToken) throws RuntimeException {
        //获取私钥
        PrivateKey privateKey = RsaUtil.getPrivateKey();
        String jwt = RsaUtil.decrypt(authorizationToken, privateKey);
        return jwt;
    }


    /**
     * @param jwtClaims
     * @return
     * @throws Exception
     * @Description 生成JWT签名串
     */
    public static String generatorJwtToken(JWTClaims jwtClaims) throws RuntimeException {
        String uuid = UUID.randomUUID().toString();
        JSONObject sub = JSONObject.parseObject(JSONObject.toJSONString(jwtClaims));
        String jwt = JWTUtil.createJWT(uuid, sub.toJSONString());
        return jwt;
    }

    /**
     * @param jwt
     * @return
     * @throws Exception
     * @Description 获取授权令牌Token
     */
    private static String getAuthorizationToken(String jwt) throws RuntimeException {
        //对jwt加密串进行RSA加密
        PublicKey publicKey = RsaUtil.getPublicKey();
        String jwtSecret = RsaUtil.encrypt(jwt, publicKey);
        return jwtSecret;
    }


    /**
     * @param authorizationToken
     * @return
     * @throws Exception
     * @Description 验证授权令牌
     */
    private static Claims validAuthorizationToken(String authorizationToken) throws Exception {
        String jwt = getJwtTicket(authorizationToken);
        //获取签名信息
        Claims claims = JWTUtil.validateJWT(jwt);
        return claims;
    }


    /**
     * @param authorizationToken
     * @return
     * @throws Exception
     * @Description 刷新票据
     */
    private static SecurityUser freshJwtTicket(String authorizationToken) throws RuntimeException {
        //jwt令牌过期,则根据提交的令牌进行加密获取用户缓存key,校验通过则签发新令牌
        String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(authorizationToken)));
        //通过票据获取用户缓存信息的key
        if (RedisUtil.hasKey(ticketKey)) {
            String loginKey = RedisUtil.get(ticketKey);
            if (StringUtils.isNotBlank(loginKey)) {
                //通过票据中的用户缓存信息key获取用户信息
                if (RedisUtil.hasKey(loginKey)) {
                    SecurityUser user = RedisUtil.get(loginKey, SecurityUser.class);
                    //更新用户缓存信息
                    addChacheUserToRedis(user);
                    //删除旧的票据
                    if (RedisUtil.hasKey(ticketKey)) RedisUtil.delete(ticketKey);
                    //记录旧票据
                    String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(authorizationToken)));
                    RedisUtil.set(oldTicketKey, user.getToken(), CacheTimeConsts.OLD_JWT_TICKET_EXPIRED_TIME);
                    return user;
                }
            }
        } else {
            //判断旧票据是否存在
            String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(authorizationToken)));
            if (RedisUtil.hasKey(oldTicketKey)) {
                //存在旧票据,证明票据还在存活期,从就票据中获取之前刷新的新票据
                String newTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(getJwtTicket(RedisUtil.get(oldTicketKey))));
                //通过新票据获取用户登录信息缓存key
                String newLoginKey = RedisUtil.get(newTicketKey);
                if (RedisUtil.hasKey(newLoginKey)) {
                    SecurityUser user = RedisUtil.get(newLoginKey, SecurityUser.class);
                    return user;
                }
            }
        }
        throw new ExpireException("令牌已过期,请重新登录");
    }
}
