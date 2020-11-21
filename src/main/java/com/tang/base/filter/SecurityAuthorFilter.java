package com.tang.base.filter;

import com.tang.base.consts.CacheKeyConsts;
import com.tang.base.security.AuthorSecurityHandle;
import com.tang.base.security.JwtAuthenticationEntryPoint;
import com.tang.base.security.SecurityUser;
import com.tang.base.security.URLAccessDeniedHandler;
import com.tang.utils.ServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 
 * @ClassName SecurityAuthorFilter
 * @Description token的校验
 * @author 芙蓉王
 * @Date Mar 7, 2020 10:35:25 AM
 * @version 1.0.0
 */
public class SecurityAuthorFilter extends OncePerRequestFilter {
    
    /**
     * 令牌异常处理
     */
    private static final JwtAuthenticationEntryPoint aet = new JwtAuthenticationEntryPoint();
    
    /**
     * URL异常处理
     */
    private static final URLAccessDeniedHandler access = new URLAccessDeniedHandler();
    
    /**
     * 权限处理
     */
    private final AuthorSecurityHandle authorSecurityHandle;
    
    public SecurityAuthorFilter(AuthorSecurityHandle authorSecurityHandle) {
        this.authorSecurityHandle = authorSecurityHandle;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.info("过滤器开始工作==========================START============================");
        try {
            //请求头获取令牌
            String authorization = ServletRequestUtil.getHeader(request, CacheKeyConsts.JWT_ISSUER);
            if(StringUtils.isBlank(authorization)) throw new BadCredentialsException("未接收到有效令牌");
            //校验已登录用户,令牌正确性
            Authentication authentication = authorSecurityHandle.getAuthentication(authorization);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            httpServletResponse.setHeader(CacheKeyConsts.JWT_ISSUER, user.getToken());
            chain.doFilter(request, response);
        }catch(AuthenticationException e) {
            aet.commence(request, response,e);
            return;
        }catch(Exception e) {
            access.handle(request, response,new AccessDeniedException("非法请求"));
            return;
        }
    }
}
