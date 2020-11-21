package com.tang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value="SysRoleEntity对象", description="角色表")
public class SysRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //超级管理员角色ID
    public static final Integer ADMIN_ROLE = 1;

    public static final Integer NORMAL_STATUS = 0;//开启
    public static final Integer DISABLE_STATUS = 1;//禁用

    @ApiModelProperty(value = "主键ID,角色记录主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色编码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "角色名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "所属部门id")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "所属部门名")
    @TableField(exist = false)
    private String deptName;


    @ApiModelProperty(value = "角色状态 0-开启 1-停用，默认为0")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
