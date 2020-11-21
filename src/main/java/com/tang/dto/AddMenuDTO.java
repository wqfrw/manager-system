package com.tang.dto;

import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AddUserDTO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author Peter
 * @Date 2020年11月17日 20:00
 * @Version 2.0.0
 **/
@Data
@Accessors(chain = true)
public class AddMenuDTO {

    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "菜单ID不能为空", groups = {Update.class})
    private Long id;

    @ApiModelProperty(value = "角色编码", required = true)
    @NotNull(message = "父级ID不能为空", groups = {Create.class, Update.class})
    private Long parentId;

    @ApiModelProperty(value = "菜单名称", required = true)
    @NotBlank(message = "菜单名称不能为空", groups = {Create.class, Update.class})
    @Length(min = 2, max = 20, message = "菜单名称为2-20个字符)", groups = {Create.class, Update.class})
    private String name;

    @ApiModelProperty(value = "菜单url", required = true)
    @Length(max = 100, message = "菜单url最长为100位)", groups = {Create.class, Update.class})
    private String url;

    @ApiModelProperty(value = "授权标识", required = true)
    @NotBlank(message = "授权标识不能为空", groups = {Create.class, Update.class})
    @Length(min = 2, max = 20, message = "授权标识为2-20个字符)", groups = {Create.class, Update.class})
    private String perms;

    @ApiModelProperty(value = "菜单类型: 0-目录 1-菜单 2-按钮", required = true)
    @NotNull(message = "菜单类型不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "菜单类型: 0-目录 1-菜单 2-按钮", groups = {Create.class, Update.class})
    @Max(value = 2, message = "菜单类型: 0-目录 1-菜单 2-按钮", groups = {Create.class, Update.class})
    private Integer type;

    @ApiModelProperty(value = "菜单图标", required = true)
    @Length(max = 100, message = "菜单图标最长为100位)", groups = {Create.class, Update.class})
    private String icon;

    @ApiModelProperty(value = "菜单权重", required = true)
    @Max(value = 1000, message = "菜单权重为0-100", groups = {Create.class, Update.class})
    @Min(value = 1000, message = "菜单权重为0-100", groups = {Create.class, Update.class})
    private Integer orderNum;

    @ApiModelProperty(value = "菜单状态: 0-启用 1-停用", required = true)
    @NotNull(message = "菜单状态不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "菜单状态: 0-启用 1-停用", groups = {Create.class, Update.class})
    @Max(value = 1, message = "菜单状态: 0-启用 1-停用", groups = {Create.class, Update.class})
    private Integer status;
}
