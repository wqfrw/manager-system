package com.tang.dto;

import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import com.tang.utils.PatternUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @ClassName AddUserDTO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author 芙蓉王
 * @Date 2020年11月17日 20:00
 * @Version 2.0.0
 **/
@Data
@Accessors(chain = true)
public class AddUserDTO {

    @ApiModelProperty(value = "主键ID,管理员用户表记录ID", required = true)
    @NotNull(message = "用户id不能为空", groups = {Update.class})
    private Long id;

    @ApiModelProperty(value = "登陆账号", required = true)
    @NotBlank(message = "账号不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = PatternUtil.USERNAMEREGEX, message = "账号为4-12个字符(字母、数字、下划线,不能以数字开头)", groups = {Create.class, Update.class})
    private String loginName;

    @ApiModelProperty(value = "用户名称", required = true)
    @Length(min = 2, max = 12, message = "用户名称为2-12个字符(字母或数字)", groups = {Create.class, Update.class})
    @NotBlank(message = "用户名称不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = PatternUtil.REALNAMEREGEX, message = "用户名称为2-12个中文字符", groups = {Create.class, Update.class})
    private String name;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "密码不能为空", groups = {Create.class})
    @Length(min = 6, max = 20, message = "密码为6-20个字符", groups = {Create.class})
    private String password;

    @ApiModelProperty(value = "性别  0-男   1-女", required = true)
    @NotNull(message = "用户性别不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "用户性别： 0-男 1-女", groups = {Create.class, Update.class})
    @Max(value = 1, message = "用户性别： 0-男 1-女", groups = {Create.class, Update.class})
    private Integer sex;

    @ApiModelProperty(value = "年龄 18~99", required = true)
    @NotNull(message = "用户年龄不能为空", groups = {Create.class, Update.class})
    @Min(value = 18, message = "用户年龄为 18~99", groups = {Create.class, Update.class})
    @Max(value = 99, message = "用户年龄为 18~99", groups = {Create.class, Update.class})
    private Integer age;

    @ApiModelProperty(value = "手机号", required = true)
    @Pattern(regexp = PatternUtil.PHONENOREGEX, message = "手机号格式错误", groups = {Create.class, Update.class})
    private String phone;

    @ApiModelProperty(value = "用户状态  0：启用  1：禁用", required = true)
    @NotNull(message = "用户状态不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "用户状态： 0-启用 1-禁用", groups = {Create.class, Update.class})
    @Max(value = 1, message = "用户状态： 0-启用 1-禁用", groups = {Create.class, Update.class})
    private Integer status;

    @ApiModelProperty(value = "角色Id", required = true)
    @NotNull(message = "角色Id不能为空", groups = {Create.class, Update.class})
    private Long roleId;
}
