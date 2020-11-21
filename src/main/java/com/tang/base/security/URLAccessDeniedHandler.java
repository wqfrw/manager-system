package com.tang.base.security;

import com.alibaba.fastjson.JSONObject;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.RR;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * <p> 认证url权限 - 登录后访问接口无权限 - 自定义403无权限响应内容 </p>
 *
 * @description : 登录过后的权限处理 【注：要和未登录时的权限处理区分开哦~】
 * @author : 芙蓉王
 * @date : 2020.2.17
 */
public class URLAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding(BaseConsts.CHARSET_UFT8);
        response.getWriter().print(JSONObject.toJSONString(RR.exception(e.getMessage())));
    }
}
