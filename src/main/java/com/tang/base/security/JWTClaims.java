package com.tang.base.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @ClassName JWTClaims
 * @Description jwt签名实体类
 * @author 芙蓉王
 * @Date Mar 3, 2020 3:25:13 PM
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "JWTClaims",description = "jwt签名实体类")
public class JWTClaims implements Serializable{
    
    private static final long serialVersionUID = -1423780129338969048L;
    
    @ApiModelProperty(value = "用户账号ID", name = "userid",required = true)
    private Long userid;
    
    @ApiModelProperty(value = "系统用户登录账号", name = "username",required = true)
    private String username;
    
}
