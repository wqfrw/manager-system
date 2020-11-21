package com.tang.dto;

import com.tang.base.validator.Create;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName EditPasswordDTO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author Peter
 * @Date 2020年11月18日 11:14
 * @Version 2.0.0
 **/
@Data
public class EditPasswordDTO {

    @ApiModelProperty(value = "原密码", name = "pwd",required = true)
    @NotBlank(message = "原密码不能为空")
    private String pwd;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "密码不能为空", groups = {Create.class})
    @Length(min = 6, max = 20, message = "密码为6-20个字符", groups = {Create.class})
    private String newPwd;
}
