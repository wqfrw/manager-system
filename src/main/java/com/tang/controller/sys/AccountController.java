package com.tang.controller.sys;


import com.tang.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账号表 前端控制器
 * </p>
 *
 * @author 芙蓉王
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/test")
public class AccountController extends BaseController {

    @GetMapping("/test")
    public String test(){
        return "success!";
    }
}
