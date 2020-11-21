package com.tang.base.consts;

/**
 * @author 芙蓉王
 * @version 1.0.0
 * @ClassName PathConsts
 * @Description 路径接口
 * @Date Dec 30, 2019 2:16:40 PM
 */
public interface PathConsts {

    /**
     * 静态资源
     */
    String STATIC_PATH = "/*.html,/**/*.html,/**/*.css,/**/*.js";

    /**
     * swagger-ui过滤的路径
     */
    String SWAGGER_PATH = "/swagger-resources/**,/swagger/**,/doc.html,/v2/api-docs/**,/v2/api-docs-ext/**,/swagger-resources-ext/**,/swagger-ui.html,/webjars/**";

    /**
     * druid监控页面过滤路径
     */
    String DRUID_HTML_PATH = "/druid/**,druid/*.html,/druid/*.css,/druid/*.js";

    /**
     * 不需要校验的资源路径
     */
//	String UNAUTHOR_PATH=".html,.js,.css,/druid,/swagger-resources,/swagger,/doc.html,/v2/api-docs,/v2/api-docs-ext,/swagger-resources-ext,/swagger-ui.html,/webjars";
    String UNAUTHOR_PATH = "/login";
}
