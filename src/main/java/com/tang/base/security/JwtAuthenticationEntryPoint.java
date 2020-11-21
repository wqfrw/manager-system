package com.tang.base.security;

import com.alibaba.fastjson.JSONObject;
import com.tang.base.consts.BaseConsts;
import com.tang.base.dto.RR;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p> 认证权限入口 - 未登录的情况下访问所有接口都会拦截到此 </p>
 *
 * @description : 前后端分离情况下返回json格式数据
 * @author : 芙蓉王
 * @date : 2020.2.17
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding(BaseConsts.CHARSET_UFT8);
        // 未登录 或 token过期
        response.getWriter().print(JSONObject.toJSONString(RR.expire(e.getMessage())));
    }
}
