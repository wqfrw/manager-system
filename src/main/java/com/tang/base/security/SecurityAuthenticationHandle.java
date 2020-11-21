//package com.tang.base.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.FilterInvocation;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * <p> 自定义对访问url进行权限认证处理 </p>
// *
// * @description :
// * @author : 芙蓉王
// * @date : 2020.2.17
// */
//public class SecurityAuthenticationHandle implements AccessDecisionManager {
//
//    private static final Logger log = LoggerFactory.getLogger(SecurityAuthenticationHandle.class);
//
//    /**
//     * @param authentication: 当前登录用户的角色信息
//     * @param object: 请求url信息
//     * @param collection: `UrlFilterInvocationSecurityMetadataSource`中的getAttributes方法传来的，表示当前请求需要的角色（可能有多个）
//     * @return: void
//     */
//    @Override
//    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> collection) throws AccessDeniedException, AuthenticationException {
////        String uri = ((FilterInvocation) object).getRequestUrl();
////        try {
//////            获取用户信息
////            SecurityUser user = (SecurityUser) authentication.getPrincipal();
////            // 数据库中所有url
////            List<String> prems = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
////            //判断资源
////            boolean hasPremission = prems.stream().anyMatch(uri::contains);
////            //判断用户是否为超级管理员
////            String loginName = user.getLoginName();
////            //判断角色
//////            boolean hasRole = loginName.equals(SUPER_ADMIN) && user.getStatus().equals(SysUser.NORMAL_STATUS);
//////            if(hasPremission || hasRole) return;
////            if(hasPremission) return;
////            throw new AccessDeniedException("未授权,无权访问!");
////        } catch (Exception e) {
////            throw new AccessDeniedException("未授权,无权访问!");
////        }
//        return;
//    }
//
//    @Override
//    public boolean supports(ConfigAttribute configAttribute) {
//        return true;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
//}
