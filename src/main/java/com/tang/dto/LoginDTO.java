package com.tang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "LoginVO",description = "登录参数实体类")
public class LoginDTO {

    @ApiModelProperty(value = "用户登录账号", name = "loginName",required = true)
    @NotBlank(message = "账号或密码不能为空")
    private String loginName;
    
    @ApiModelProperty(value = "用户登录密码", name = "password",required = true)
    @NotBlank(message = "账号或密码不能为空")
    private String password;
}
