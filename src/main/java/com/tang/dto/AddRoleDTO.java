package com.tang.dto;

import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @ClassName AddUserDTO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author Peter
 * @Date 2020年11月17日 20:00
 * @Version 2.0.0
 **/
@Data
@Accessors(chain = true)
public class AddRoleDTO {

    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "角色id不能为空", groups = {Update.class})
    private Long id;

    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank(message = "角色编码不能为空", groups = {Create.class, Update.class})
    @Length(min = 2, max = 10, message = "角色编码为2-10个字符", groups = {Create.class, Update.class})
    private String code;

    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空", groups = {Create.class, Update.class})
    @Length(min = 2, max = 20, message = "角色名称为2-20个字符)", groups = {Create.class, Update.class})
    private String name;

    @ApiModelProperty(value = "部门ID", required = true)
    @NotNull(message = "部门ID不能为空", groups = {Create.class, Update.class})
    private Long deptId;

    @ApiModelProperty(value = "角色状态: 0-启用 1-停用", required = true)
    @NotNull(message = "角色状态不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "角色状态: 0-启用 1-停用", groups = {Create.class, Update.class})
    @Max(value = 1, message = "角色状态: 0-启用 1-停用", groups = {Create.class, Update.class})
    private Integer status;
}
