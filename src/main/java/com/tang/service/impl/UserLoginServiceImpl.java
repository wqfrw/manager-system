package com.tang.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tang.base.factory.ThreadHolder;
import com.tang.base.security.AuthorHandle;
import com.tang.base.security.SecurityUser;
import com.tang.dto.LoginDTO;
import com.tang.entity.SysDeptEntity;
import com.tang.entity.SysMenuEntity;
import com.tang.entity.SysRoleEntity;
import com.tang.entity.SysUserEntity;
import com.tang.mapper.SysDeptMapper;
import com.tang.mapper.SysMenuMapper;
import com.tang.mapper.SysRoleMapper;
import com.tang.mapper.SysUserMapper;
import com.tang.service.UserLoginService;
import com.tang.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户登录接口
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserLoginServiceImpl(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /**
     * Security登录 认证
     */
    @Override
    public UserVO login(LoginDTO dto) throws AuthenticationException {
        //根据用户登录账号获取用户信息
        SysUserEntity user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getLoginName, dto.getLoginName()));
        if (user == null) throw new UsernameNotFoundException("账号或密码错误");
        //校验账号状态
        if (SysUserEntity.DISABLE_STATUS.equals(user.getStatus())) throw new LockedException("账号已禁用,请联系管理员");

        //密码加密
        String password = new MD5(user.getSalt().getBytes()).digestHex(dto.getPassword().getBytes());
        //把当前用户信息存储在当前线程中,方便后面的方法使用
        ThreadHolder.push(JSONObject.toJSONString(user));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginName(), password);
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("账号或密码错误");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //获取存入缓存信息
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        //操作登录成功信息,并清除敏感信息
        AuthorHandle.loginSuccessHandle(securityUser);
        //封装登录响应结果
        return new UserVO(securityUser);
    }

    /**
     * 重写用户校验接口
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        SecurityUser securityUser = new SecurityUser();
        //根据用户登录账号获取用户信息
        SysUserEntity user = JSONObject.parseObject(ThreadHolder.peek(), SysUserEntity.class);
        BeanUtils.copyProperties(user, securityUser);
        //从线程中获取到数据后需清空当前线程
        ThreadHolder.poll();

        //查询登录用户角色
        SysRoleEntity role = sysRoleMapper.selectByUserId(securityUser.getId());
        if (role == null || StringUtils.isEmpty(role.getName())) throw new DisabledException("尚未属于任何角色,请联系管理员");
        if (role.getStatus().equals(SysRoleEntity.DISABLE_STATUS)) throw new DisabledException("该用户所属角色已禁用,请联系管理员");
        securityUser.setRole(role);

        //通过角色id获取所有角色具备的正常开启的菜单权限资源
        List<SysMenuEntity> menus = sysMenuMapper.selectByRoleId(role.getId());
        if (CollectionUtils.isEmpty(menus) || menus.stream().noneMatch(v -> SysMenuEntity.NORMAL_STATUS.equals(v.getStatus())))
            throw new DisabledException("尚未绑定任何启用的菜单,请联系管理员");
        securityUser.setMenus(menus);

        //查询登录用户所属部门
        SysDeptEntity dept = sysDeptMapper.selectById(role.getDeptId());
        securityUser.setDept(dept);

        //获取授权标识
        List<String> perms = menus.stream().map(SysMenuEntity::getPerms).filter(e -> StringUtils.isNotBlank(e)).collect(Collectors.toList());
        securityUser.setPrems(perms);
        return securityUser;
    }
}
