package com.tang.base.consts;

/**
 * 
 * @ClassName BaseConsts
 * @Description 基本常量接口类
 * @author 芙蓉王
 * @Date Mar 3, 2020 10:55:07 AM
 * @version 1.0.0
 */
public interface BaseConsts {

    /**
     * UTF-8编码
     */
    String CHARSET_UFT8="UTF-8";
    
    /**  加密算法 */
    String hashAlgorithmName = "SHA-256";
    
    /** 默认分页页码 */
    Integer DEFAULT_PAGE = 1;
    
    /** 默认分页条数  */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 请求头类型：
     * application/x-www-form-urlencoded ： form表单格式
     * application/json ： json格式
     */
    String REQUEST_HEADERS_CONTENT_TYPE = "application/json;charset=utf-8";
    
    /**
     * 请求类型
     */
    String REQUEST_METHOD="POST";
}
