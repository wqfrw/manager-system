package com.tang.base.config;

import com.tang.base.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * 
 * @ClassName FilterConfig
 * @Description 过滤器配置类
 * @author 芙蓉王
 * @Date Dec 13, 2019 11:51:58 AM
 * @version 1.0.0
 */
@Configuration
public class FilterConfig {

	@SuppressWarnings({"rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

}
