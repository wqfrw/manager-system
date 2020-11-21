package com.tang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后台用户表
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="SysUserEntity对象", description="后台用户表")
public class SysUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户状态  禁用
    public static final Integer DISABLE_STATUS = 1;
    //用户状态  正常
    public static final Integer NORMAL_STATUS = 0;

    @ApiModelProperty(value = "主键ID,管理员用户表记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "登陆账号")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty(value = "用户名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "登录密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "密码加密盐")
    @TableField("salt")
    private String salt;

    @ApiModelProperty(value = "性别  0-男   1-女")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "用户状态  0：启用  1：禁用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "是否为第一次登入，0是，1不是")
    @TableField("loginstatus")
    private Integer loginstatus;
}
