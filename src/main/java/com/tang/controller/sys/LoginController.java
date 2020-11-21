package com.tang.controller.sys;

import com.tang.base.annotation.SysLog;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.RR;
import com.tang.base.enums.LogEnum;
import com.tang.base.security.AuthorHandle;
import com.tang.controller.BaseController;
import com.tang.dto.LoginDTO;
import com.tang.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统管理-用户登录与登出接口
 */
@Api(value = "系统管理-用户登录与登出接口", tags = "系统管理-用户登录与登出接口")
@RestController
public class LoginController extends BaseController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * @param dto
     * @return
     * @throws Exception
     * @Description 系统管理-用户登录接口
     */
    @SysLog(logEnum = LogEnum.LOGIN, filterParams = {"password","token"})
    @PostMapping(value = "/login", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "系统管理-用户登录接口", notes = "系统管理-用户登录接口", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR login(@Validated @RequestBody LoginDTO dto) {
        return RR.success("登录成功", userLoginService.login(dto));
    }


    /**
     * @return
     * @throws Exception
     * @Description 权限管理-用户登出接口
     */
    @SysLog(logEnum = LogEnum.LOGIN_OUT)
    @PostMapping(value = "/user/logout", produces = BaseConsts.REQUEST_HEADERS_CONTENT_TYPE)
    @ApiOperation(value = "权限管理-用户登出接口", notes = "权限管理-用户登出接口", httpMethod = BaseConsts.REQUEST_METHOD, response = RR.class)
    public RR logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            AuthorHandle.destoryUserFromRedis(request, response);
        } catch (Exception e) {
            log.info("用户登出销毁用户信息异常:{}", e.getMessage());
        }
        return RR.success("登出成功");
    }
}
