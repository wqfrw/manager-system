package com.tang.dto;

import com.tang.base.dto.PagerQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName UserDTO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Author 芙蓉王
 * @Date 2020年11月17日 17:00
 * @Version 2.0.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RoleDTO", description = "系统管理-系统角色分页查询DTO类")
public class RoleDTO extends PagerQuery {

    @ApiModelProperty(value = "角色名称", name = "name")
    private String name;

}
