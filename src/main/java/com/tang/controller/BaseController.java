package com.tang.controller;

import com.tang.base.consts.CacheKeyConsts;
import com.tang.base.security.AuthorHandle;
import com.tang.base.security.SecurityUser;
import com.tang.entity.SysRoleEntity;
import com.tang.utils.MD5Util;
import com.tang.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 抽象接口父类
 */
public abstract class BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * @return
     * @Description 获取当前登录用户信息
     */
    protected SecurityUser getSecurityUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * @return
     * @Description 获取当前登录用户ID
     */
    protected Long getUserId() {
        return getSecurityUser().getId();
    }

    /**
     * @return
     * @Description 获取当前登录用户账号
     */
    protected String getUsername() {
        return getSecurityUser().getLoginName();
    }

    /**
     * @return
     * @Description 校验当前用户是否为管理员
     */
    protected boolean isAdmin() {
        return getSecurityUser().getRole().getId().equals(SysRoleEntity.ADMIN_ROLE.longValue());
    }

    /**
     * 功能描述: 当前用户退出登录
     *
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月18日 15:49:04
     * @param
     * @return: void
     **/
    protected void loginOut() {
        SecurityUser user = getSecurityUser();
        //生成用户登录缓存key
        String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + user.getLoginName());
        //生成用户的登录票据key
        String ticketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(AuthorHandle.getJwtTicket(user.getToken())));
        //旧票据key
        String oldTicketKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_OLD_JWT_TICKET_KEY + MD5Util.md5toUpCase_16Bit(AuthorHandle.getJwtTicket(user.getToken())));
        //清空缓存信息
        if (RedisUtil.hasKey(loginKey)) RedisUtil.delete(loginKey);
        //清空缓存票据
        if (RedisUtil.hasKey(ticketKey)) RedisUtil.delete(ticketKey);
        //清空缓存旧票据
        if (RedisUtil.hasKey(oldTicketKey)) RedisUtil.delete(oldTicketKey);
    }

    /**
     * 功能描述: 指定用户退出登录 这里只能删除该用户的登录缓存key,登录票据key让其自然过期
     *
     * @param loginName
     * @创建人: 芙蓉王
     * @创建时间: 2020年11月18日 15:37:38
     * @return: void
     **/
    protected void loginOut(String loginName) {
        //生成用户登录缓存key
        String loginKey = StringUtils.upperCase(CacheKeyConsts.ADMIN_USER_LOGIN_TOKEN_KEY + loginName);
        //清空缓存信息
        if (RedisUtil.hasKey(loginKey)) RedisUtil.delete(loginKey);
    }
}
