package com.tang.base.exception;

/**
 *
 * @ClassName RequestDataException
 * @Description 请求参数异常
 * @author 芙蓉王
 * @Date Apr 1, 2020 8:13:55 PM
 * @version 1.0.0
 */
public class RequestDataException extends Exception{

    private static final long serialVersionUID = 4108720646637888538L;

    public RequestDataException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public RequestDataException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }



}
