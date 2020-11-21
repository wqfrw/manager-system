package com.tang.base.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private boolean enabled;
    

    @Bean
    public Docket permissionRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .protocols(Sets.newHashSet("http", "https"))
                .groupName("system-系统管理接口模块")
                .apiInfo(apiInfo("后台管理系统接口 V1.0版本"))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.tang.controller.sys"))
                .paths(PathSelectors.any()).build();
    }


    
    /**
     * 设置API显示信息
     *
     * @param desc
     * @return
     */
    private ApiInfo apiInfo(String desc) {
        return new ApiInfoBuilder()
                //页面标题
                .title(desc)
                //描述
                .description("后台管理系统接口服务 V1.0版本")
                //版本号
                .version("1.0")
                .build();
    }
}
