package com.tang.base.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 
 * @ClassName ExpireException
 * @Description 令牌过期异常
 * @author 芙蓉王
 * @Date Dec 28, 2019 8:41:57 PM
 * @version 1.0.0
 */
@Getter
public class ExpireException extends RuntimeException{
	
	private static final long serialVersionUID = -4949215322363800626L;

    private int status = HttpStatus.BAD_REQUEST.value();
	
	public ExpireException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpireException(String message) {
		super(message);
	}
	
	public ExpireException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
	
	
}
