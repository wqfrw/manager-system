package com.tang.dto;

import com.tang.base.dto.PagerQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@ApiModel(value = "UserDTO", description = "系统管理-系统用户分页查询DTO类")
public class UserDTO extends PagerQuery {

    @ApiModelProperty(value = "用户名称", name = "name")
    private String name;

    @ApiModelProperty(value = "用户状态", name = "status")
    @Min(value = 0, message = "用户状态: 0-启用 1-禁用")
    @Max(value = 1, message = "用户状态: 0-启用 1-禁用")
    private Integer status;

    @ApiModelProperty(value = "起始时间", name = "startTime")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", name = "endTime")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;
}
