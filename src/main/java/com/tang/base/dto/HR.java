package com.tang.base.dto;

/**
 * 
 * @ClassName HR
 * @Description Http请求封装返回结果
 * @author
 * @Date Jan 17, 2020 10:24:48 PM
 * @version 1.0.0
 */
public class HR {
	
	/**
	 * 成功状态值
	 */
	public static final int SUCCESS_STATUS = 200;

	private int status;
	
	private String response;
	
	public HR() {
		super();
	}

	public HR(int status, String response) {
		super();
		this.status = status;
		this.response = response;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "HR [status=" + status + ", response=" + response + "]";
	}
}
