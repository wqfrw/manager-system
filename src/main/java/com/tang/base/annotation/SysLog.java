package com.tang.base.annotation;

import com.tang.base.enums.LogEnum;

import java.lang.annotation.*;

/**
 * 系统操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    LogEnum logEnum();

    /**
     * 过滤的参数名数组  包括请求参数和返回参数
     * 存到日志表里显示为:******
     */
    String[] filterParams() default {};
}
