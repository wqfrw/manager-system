package com.tang.vo;

import com.tang.base.security.SecurityUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserVO", description = "用户VO类")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 5824443737697677963L;

    @ApiModelProperty(value = "用户ID", name = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "登录账号", name = "loginName", required = true)
    private String loginName;

    @ApiModelProperty(value = "用户名", name = "name", required = true)
    private String name;

    @ApiModelProperty(value = "用户状态 0-启用 1-停用", name = "status", required = true)
    private Integer status;

    @ApiModelProperty(value = "性别", name = "sex", required = true)
    private Integer sex;

    @ApiModelProperty(value = "年龄", name = "age", required = true)
    private Integer age;

    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    private String phone;

    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private Date createTime;

    @ApiModelProperty(value = "登录令牌", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "角色id", name = "roleId", required = true)
    private Long roleId;

    @ApiModelProperty(value = "角色", name = "roleName", required = true)
    private String roleName;

    @ApiModelProperty(value = "部门id", name = "deptId", required = true)
    private Long deptId;

    @ApiModelProperty(value = "部门名", name = "deptName", required = true)
    private String deptName;

    public UserVO(SecurityUser user) {
        this.id = user.getId();
        this.loginName = user.getLoginName();
        this.name = user.getName();
        this.status = user.getStatus();
        this.sex = user.getSex();
        this.age = user.getAge();
        this.phone = user.getPhone();
        this.roleId = user.getRole().getId();
        this.roleName = user.getRole().getName();
        this.deptId = user.getDept().getId();
        this.deptName = user.getDept().getName();
        this.createTime = user.getCreateTime();
        this.token = user.getToken();
    }
}
