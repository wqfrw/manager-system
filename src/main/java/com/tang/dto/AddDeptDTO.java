package com.tang.dto;

import com.tang.base.validator.Create;
import com.tang.base.validator.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统部门实体类
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AddDeptDTO", description = "系统部门实体类")
public class AddDeptDTO implements Serializable {

    private static final long serialVersionUID = -6310331730721778951L;

    @ApiModelProperty(value = "主键",required = true)
    @NotNull(message = "id不能为空",groups = Update.class)
    private Long id;

    @ApiModelProperty(value = "部门名称",required = true)
    @NotBlank(message = "部门名称不能为空",groups = {Create.class,Update.class})
    @Length(min = 2,max = 10, message = "部门名称为2-10个字符",groups = {Create.class,Update.class})
    private String name;

    @ApiModelProperty(value = "备注")
    @Length(max = 30, message = "备注最长为30个字符",groups = {Create.class,Update.class})
    private String rmk;
}
