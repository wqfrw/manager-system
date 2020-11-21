package com.tang.dto;

import com.tang.base.dto.PagerQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@ApiModel(value = "DeptDTO", description = "系统管理-系统部门分页查询DTO类")
public class DeptDTO extends PagerQuery {

    @ApiModelProperty(value = "部门名称", name = "name")
    private String name;

}
