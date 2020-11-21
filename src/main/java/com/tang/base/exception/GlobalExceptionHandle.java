package com.tang.base.exception;

import com.tang.base.dto.RR;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author 芙蓉王
 * @version 1.0.0
 * @ClassName GlobalExceptionHandle
 * @Description 全局异常处理类
 * @Date Mar 3, 2020 3:59:07 PM
 */
@RestControllerAdvice
public class GlobalExceptionHandle {

    /**
     * @param e
     * @return
     * @Description 未知异常处理
     */
    @ExceptionHandler(Exception.class)
    public RR handleException(Exception e) {
        e.printStackTrace();
        return RR.exception("系统异常,请联系管理员");
    }

    /**
     * @param e
     * @return
     * @Description 系统业务服务异常
     */
    @ExceptionHandler(ServerException.class)
    public RR handleServerException(ServerException e) {
        return RR.exception(e.getMessage());
    }

    /**
     * @param e
     * @return
     * @Description 令牌校验异常
     */
    @ExceptionHandler(ExpireException.class)
    public RR handleExpireException(ExpireException e) {
        return RR.expire(e.getMessage());
    }

    /**
     * @param e
     * @return
     * @Description 请求参数错误
     */
    @ExceptionHandler(RequestDataException.class)
    public RR handleRequestDataException(RequestDataException e) {
        return RR.exception("请求参数异常:[" + e.getMessage() + "]");
    }

    /**
     * @param e
     * @return
     * @Description 请求参数异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RR handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        return RR.exception("请求参数异常:[(" + fieldError.getField() + ") " + fieldError.getDefaultMessage() + "]");
    }


    /**
     * @param e
     * @return
     * @Description security 权限注解 无权访问
     */
    @ExceptionHandler(AccessDeniedException.class)
    public RR handleAccessDeniedException(AccessDeniedException e) {
//        return RR.forbidden(e.getMessage());
        return RR.forbidden("未授权,无权访问");
    }


    /**
     * @param e
     * @return
     * @Description 登录异常处理
     */
    @ExceptionHandler(AuthenticationException.class)
    public RR handleAuthenticationException(AuthenticationException e) {
        return RR.exception(e.getMessage());
    }

}
