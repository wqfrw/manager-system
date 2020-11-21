package com.tang.base.factory;

/**
 * 
 * @ClassName ThreadHolder
 * @Description 线程工具类
 * @author 芙蓉王
 * @Date Feb 12, 2020 8:59:38 PM
 * @version 1.0.0
 */
public class ThreadHolder {
	/**
	 * 线程句柄
	 */
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();
	
	/**
	 * 
	 * @Description 获取当前线程数据
	 * @return
	 */
	public static String peek() {
		return CONTEXT_HOLDER.get();
	}
	
	/**
	 * 
	 * @Description 设置当前线程
	 * @param data
	 */
	public static void push(String data) {
		CONTEXT_HOLDER.set(data);
	}
	
	/**
	 * 
	 * @Description 清空当前线程
	 */
	public static void poll() {
		CONTEXT_HOLDER.remove();
	}
	
}
