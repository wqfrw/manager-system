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
 * 系统日志表
 * </p>
 *
 * @author peter
 * @since 2020-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value="SysLogEntity对象", description="系统日志表")
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "菜单名")
    @TableField("menu")
    private String menu;

    @ApiModelProperty(value = "功能")
    @TableField("function")
    private String function;

    @ApiModelProperty(value = "请求参数")
    @TableField("parameters")
    private String parameters;

    @ApiModelProperty(value = "返回结果")
    @TableField("result")
    private String result;

    @ApiModelProperty(value = "操作ip")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "操作用户账号")
    @TableField("creater")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;


}
