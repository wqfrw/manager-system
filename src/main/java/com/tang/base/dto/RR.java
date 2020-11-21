package com.tang.base.dto;

import java.io.Serializable;

/**
 * 
 * @ClassName RR
 * @Description 响应结果封装类
 */
public class RR implements Serializable{
    
    private static final long serialVersionUID = -6182413082562035842L;

    private int status;
    
    private String msg;
    
    private Object data;

    public RR(Object data) {
        this.data = data;
    }

    public RR(int status, String message) {
        super();
        this.status = status;
        this.msg = message;
    }

    public RR(int status, String message, Object data) {
        super();
        this.status = status;
        this.msg = message;
        this.data = data;
    }
    
    public static RR success() {
        return new RR(RC.SUCCESS_STATUS, RC.SUCCESS_MESSAGE);
    }
    
    public static RR success(String message) {
        return new RR(RC.SUCCESS_STATUS, message);
    }
    
    public static RR success(Object data) {
        return new RR(RC.SUCCESS_STATUS, RC.SUCCESS_MESSAGE, data);
    }
    
    public static RR success(String message, Object data) {
        return new RR(RC.SUCCESS_STATUS, message, data);
    }
    
    public static RR failure() {
        return new RR(RC.FAILURE_STATUS, RC.FAILURE_MESSAGE);
    }
    
    public static RR failure(String message) {
        return new RR(RC.FAILURE_STATUS, message);
    }
    
    public static RR failure(Object data) {
        return new RR(RC.FAILURE_STATUS, RC.SUCCESS_MESSAGE, data);
    }
    
    public static RR failure(String message, Object data) {
        return new RR(RC.FAILURE_STATUS, message, data);
    }
    
    public static RR process() {
        return new RR(RC.PROCESS_STATUS, RC.PROCESS_MESSAGE);
    }
    
    public static RR process(String message) {
        return new RR(RC.PROCESS_STATUS, message);
    }
    
    public static RR process(Object data) {
        return new RR(RC.PROCESS_STATUS, RC.PROCESS_MESSAGE, data);
    }
    
    public static RR process(String message, Object data) {
        return new RR(RC.PROCESS_STATUS, message, data);
    }
    
    public static RR exception() {
        return new RR(RC.EXCEPTION_STATUS, RC.EXCEPTION_MESSAGE);
    }
    
    public static RR exception(String message) {
        return new RR(RC.EXCEPTION_STATUS, message);
    }
    
    public static RR expire() {
        return new RR(RC.EXPIRED_STATUS, RC.EXPIRED_MESSAGE);
    }
    
    public static RR expire(String message) {
        return new RR(RC.EXPIRED_STATUS, message);
    }
    
    public static RR expire(Object data) {
        return new RR(RC.EXPIRED_STATUS, RC.EXPIRED_MESSAGE, data);
    }
    
    public static RR expire(String message, Object data) {
        return new RR(RC.EXPIRED_STATUS, message, data);
    }
    
    public static RR forbidden(String message, Object data) {
        return new RR(RC.FORBIDDEN_STATUS, message, data);
    }

    public static RR forbidden() {
        return new RR(RC.FORBIDDEN_STATUS, RC.FORBIDDEN_MESSAGE);
    }
    
    public static RR forbidden(String message) {
        return new RR(RC.FORBIDDEN_STATUS, message);
    }
    
    public static RR forbidden(Object data) {
        return new RR(RC.FORBIDDEN_STATUS, RC.FORBIDDEN_MESSAGE, data);
    }
    
    
    public int getStatus() {
        return status;
    }

    
    public void setStatus(int status) {
        this.status = status;
    }

    
    public String getMessage() {
        return msg;
    }

    
    public void setMessage(String message) {
        this.msg = message;
    }

    
    public Object getData() {
        return data;
    }

    
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RR{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
