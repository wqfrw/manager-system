package com.tang.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName LogEnum
 * @Description TODO(日志类型枚举类)
 * @Author 芙蓉王
 * @Date 2020年11月21日 11:30
 * @Version 2.0.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LogEnum {

    LOGIN("系统管理", "登录"),
    LOGIN_OUT("系统管理", "登出"),
    EDIT_PWD("系统管理", "修改登录密码"),
    ADD_USER("用户管理", "添加用户"),
    EDIT_USER("用户管理", "编辑用户"),
    DELETE_USER("用户管理", "删除用户"),
    ADD_ROLE("角色管理", "添加角色"),
    EDIT_ROLE("角色管理", "编辑角色"),
    DELETE_ROLE("角色管理", "删除角色"),
    AUTH_ROLE("角色管理", "角色授权"),
    ADD_DEPT("部门管理", "新增部门"),
    EDIT_DEPT("部门管理", "编辑部门"),
    DELETE_DEPT("部门管理", "删除部门"),
    ADD_MENU("菜单管理", "添加菜单"),
    EDIT_MENU("菜单管理", "编辑菜单"),
    DELETE_MENU("菜单管理", "删除菜单"),;

    private String menu;

    private String function;
}
