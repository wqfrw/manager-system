package com.tang.vo.excel;

import com.tang.base.annotation.ExcelField;
import lombok.Data;

import java.util.Date;


/**
 * 用户导出实体类
 */
@Data
public class UserExport {

    @ExcelField(title = "登录账号", align = 2, sort = 1)
    private String loginName;

    @ExcelField(title = "用户名", align = 2, sort = 2)
    private String name;

    @ExcelField(title = "用户状态 0-启用 1-停用", align = 2, sort = 3)
    private Integer status;

    @ExcelField(title = "性别", align = 2, sort = 4)
    private Integer sex;

    @ExcelField(title = "年龄", align = 2, sort = 5)
    private Integer age;

    @ExcelField(title = "手机号", align = 2, sort = 6)
    private String phone;

    @ExcelField(title = "创建时间", align = 2, sort = 7)
    private Date createTime;

    @ExcelField(title = "角色", align = 2, sort = 8)
    private String roleName;

    @ExcelField(title = "部门名", align = 2, sort = 9)
    private String deptName;
}
