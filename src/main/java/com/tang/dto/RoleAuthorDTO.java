package com.tang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RoleAuthorDTO",description = "角色管理-角色授权DTO类")
public class RoleAuthorDTO {

    @ApiModelProperty(value = "角色Id", name = "roleId",required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    
    @ApiModelProperty(value = "菜单列表ID列表", name = "menuIds")
    private List<Long> menuIds = new ArrayList<Long>();//菜单ID列表
}
