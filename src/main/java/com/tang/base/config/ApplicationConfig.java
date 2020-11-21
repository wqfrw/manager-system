package com.tang.base.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName ApplicationConfig
 * @Description Spring上下文配置中心
 * @author 芙蓉王
 * @Date Dec 13, 2019 12:14:15 PM
 * @version 1.0.0
 */
@Configuration
public class ApplicationConfig implements ApplicationContextAware {
    
	/**
	 * Spring上下文
	 */
	private static ApplicationContext applicationContext;
	
	/**
	 * SpringBoot环境
	 */
	private static AbstractEnvironment environment;
	
	 /**
     * 工程类路径配置
     */
    private static final String PROJECT_CLASS_PATH  = "java.class.path";
    
    /**
     * 类路径分隔符正则式
     */
    private static final Pattern CLASSPATH_SEPARATOR_REGEX=Pattern.compile(File.pathSeparator);
	
	/**
	 * @return the applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * @param applicationContext the applicationContext to set
	 */
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationConfig.applicationContext = applicationContext;
	}
	
	public static AbstractEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(AbstractEnvironment environment) {
		ApplicationConfig.environment = environment;
	}

	/**
	 * 获取Spring上下文中的Bean对象
	 * @param beanName BeanId
	 * @return 对象类型
	 */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	/**
	 * 获取Spring上下文中的Bean对象
	 * @param beanType Bean类型
	 * @return 泛化类型
	 */
	public static <R> R getBean(Class<R> beanType) {
		return applicationContext.getBean(beanType);
	}
	
	/**
	 * 获取Spring上下文中的Bean对象
	 * @param beanName BeanId
	 * @param beanType Bean类型
	 * @return 泛化类型
	 */
	public static <R> R getBean(String beanName,Class<R> beanType) {
		return applicationContext.getBean(beanName,beanType);
	}
	
	/**
	 * 获取当前类路径
	 * 返回此方法调用方的类路径目录
	 * @return 字串类型
	 */
	public static String getCurrentClassPath(){
		String callClassName=Thread.currentThread().getStackTrace()[2].getClassName();
		String callPkgPath=callClassName.substring(0, callClassName.lastIndexOf('.')).replace('.', File.separatorChar);
		return new StringBuilder(getClassPathRoot()).append(File.separatorChar).append(callPkgPath).toString();
	}
	
	/**
	 * 获取参数包对应的类路径目录
	 * @param packagePath 包路径
	 * @return 字串类型
	 */
	public static String getPackageClassPath(String packagePath){
		String packageDir=packagePath.replace('.', File.separatorChar);
		return new StringBuilder(getClassPathRoot()).append(File.separatorChar).append(packageDir).toString();
	}
	
	/**
	 * 获取工程类路径根目录
	 * @return 字串类型
	 */
	public static String getClassPathRoot(){
		try {
			return new File(ApplicationConfig.class.getResource("/").toURI()).getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getClassPathList().get(0);
	}
	
	/**
	 * 获取类路径目录列表
	 * @return 路径列表
	 */
	public static List<String> getClassPathList(){
		String classpaths= environment.getSystemEnvironment().get(PROJECT_CLASS_PATH).toString();
		return Arrays.asList(CLASSPATH_SEPARATOR_REGEX.split(classpaths));
	}
	
}
