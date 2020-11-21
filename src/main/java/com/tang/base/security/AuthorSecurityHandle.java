package com.tang.base.security;

import com.tang.base.exception.ExpireException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 
 * @ClassName AuthorSecurityHandle
 * @Description 权限处理类
 * @author 芙蓉王
 * @Date Mar 7, 2020 12:19:39 PM
 * @version 1.0.0
 */
@Component
public class AuthorSecurityHandle {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorSecurityHandle.class);

    /**
     * 
     * @Description 授权
     * @param authorization
     * @return
     */
    public Authentication getAuthentication(String authorization) throws AuthenticationException {
        try {
            //从缓存获取用户信息
            SecurityUser user = AuthorHandle.getCacheUser(authorization);
            if(user == null) throw new UsernameNotFoundException("非法令牌,查询用户信息不存在");
            //根据token获取用户缓存信息
            Collection<? extends GrantedAuthority> authorities =  user.getAuthorities();
            return new UsernamePasswordAuthenticationToken(user, authorization, authorities);
        }catch(ExpireException e) {
            throw new CredentialsExpiredException("令牌已过期,请重新登录");
        }catch (Exception e) {
            log.info("校验token异常:{}",e.getMessage());
            throw e;
        }
     }
}
