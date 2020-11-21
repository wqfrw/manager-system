package com.tang.base.security;

import com.tang.entity.SysDeptEntity;
import com.tang.entity.SysMenuEntity;
import com.tang.entity.SysRoleEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 安全认证用户详情 </p>
 *
 * @description :
 * @author : 芙蓉王
 * @date : 2020.2.17
 */
@Getter
@Setter
@ApiModel(value = "SecurityUser",description = "缓存用户信息")
public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = 2503461170316674714L;

    @ApiModelProperty(value = "用户ID", name = "id",required = true)
    private Long id;

    @ApiModelProperty(value = "用户名", name = "name",required = true)
    private String name;

    @ApiModelProperty(value = "登录账号", name = "loginName",required = true)
    private String loginName;

    @ApiModelProperty(value = "登录账号密码", name = "password",required = true)
    private String password;

    @ApiModelProperty(value = "登录账号密码加密盐", name = "salt",required = true)
    private String salt;

    @ApiModelProperty(value = "用户状态:0-启用 1-禁用", name = "status",required = true)
    private Integer status;

    @ApiModelProperty(value = "性别  0-男   1-女")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "创建时间", name = "createTime",required = true)
    private Date createTime;

    @ApiModelProperty(value = "是否为首次登录", name = "loginstatus",required = true)
    private Integer loginstatus;

    @ApiModelProperty(value = "用户角色", name = "roleName",required = true)
    private SysRoleEntity role;

    @ApiModelProperty(value = "所属部门", name = "dept",required = true)
    private SysDeptEntity dept;

    /**
     * 当前用户具备的菜单
     */
    private List<SysMenuEntity> menus;

    /**
     * 授权标识
     */
    private List<String> prems;

    @ApiModelProperty(value = "登录令牌", name = "token",required = true)
    private String token;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 将当前用户的权限封装成GrantedAuthority对象放入security中
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(CollectionUtils.isEmpty(this.prems)) return null;
        return this.prems.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
