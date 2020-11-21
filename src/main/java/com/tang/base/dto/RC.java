package com.tang.base.dto;

/**
 * 
 * @ClassName RC
 * @Description 响应状态码
 */
public interface RC {

    int SUCCESS_STATUS = 10000;
    
    int FAILURE_STATUS = 20000;
    
    int PROCESS_STATUS = 30000;
    
    int EXCEPTION_STATUS = 40000;
    
    int EXPIRED_STATUS = 24000;
    
    int FORBIDDEN_STATUS = 40004;//无权访问
    
    String SUCCESS_MESSAGE="success";//成功
    
    String FAILURE_MESSAGE="failure";//失败
    
    String PROCESS_MESSAGE="process";//处理中
    
    String EXCEPTION_MESSAGE="exception";//异常
    
    String EXPIRED_MESSAGE="expired";//过期
    
    String FORBIDDEN_MESSAGE="forbidden";//无权
}

