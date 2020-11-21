package com.tang.base.exception;

/**
 * 
 * @ClassName ServiceException
 * @Description 业务异常
 * @author 芙蓉王
 * @Date Dec 13, 2019 10:53:15 AM
 * @version 1.0.0
 */
public class ServerException extends Exception{
	
	private static final long serialVersionUID = 5355604012843757129L;

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}
	
}
