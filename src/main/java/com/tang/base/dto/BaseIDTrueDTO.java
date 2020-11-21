package com.tang.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 
 * @ClassName BaseIDTrueDTO
 * @Description ID不为空的父级VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BaseIDTrueDTO",description = "ID不为空的父级DTO类")
public class BaseIDTrueDTO {

    @ApiModelProperty(value = "通用型的ID参数", name = "id", required = true)
    @NotNull(message = "ID不能为空")
    private Long id;
}
