package com.tang.base.config;

import com.tang.base.consts.BaseConsts;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 
 * @ClassName WebConfig
 * @Description MVC配置类
 * @author 芙蓉王
 * @Date Mar 2, 2020 5:04:36 PM
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    /**
     * 静态资源配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
    }

    /**
     * Http消息转换器配置
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(Charset.forName(BaseConsts.CHARSET_UFT8)));
    }
   
    
}
