package com.tang.dto.excel;

import com.tang.base.annotation.ExcelField;
import lombok.Data;

/**
 * @ClassName UserImport
 * @Description TODO(用户导入实体类)
 * @Author 芙蓉王
 * @Date 2020年11月21日 17:31
 * @Version 2.0.0
 **/
@Data
public class UserImport {

    @ExcelField(title = "登录账号", align = 2, sort = 1)
    private String loginName;

    @ExcelField(title = "用户名", align = 2, sort = 2)
    private String name;

    @ExcelField(title = "密码", align = 2, sort = 3)
    private String password;

    @ExcelField(title = "手机号", align = 2, sort = 4)
    private String phone;

    @ExcelField(title = "年龄", align = 2, sort = 5)
    private Integer age;
}
