package com.tang.base.config;

import com.tang.base.consts.PathConsts;
import com.tang.base.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 *  <p> Security 核心配置类 </p>
 *
 * @author : 芙蓉王
 * @date : 2020.2.17
 * @version：  <br/>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthorSecurityHandle authorSecurityHandle;

    public SecurityConfig(AuthorSecurityHandle authorSecurityHandle) {
        super();
        this.authorSecurityHandle = authorSecurityHandle;
    }

    @Value("${swagger.enabled}")
    private boolean swaggerEnabled;

    @Value("${druid.html.enabled}")
    private boolean druidHTMLEnabled;


    /**
     * 全局配置
     */
    @Override
    public void configure(WebSecurity web){
        //静态资源
        web.ignoring().antMatchers(HttpMethod.GET, PathConsts.STATIC_PATH.split(","));

        //swagger文档
        if (swaggerEnabled) {
            web.ignoring().antMatchers(PathConsts.SWAGGER_PATH.split(","));
        }
        //druid sql 监控页面
        if (druidHTMLEnabled) {
            web.ignoring().antMatchers(PathConsts.DRUID_HTML_PATH.split(","));
        }

        //放行OPTIONS请求
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        //放行登录接口
        web.ignoring().antMatchers(HttpMethod.POST, PathConsts.UNAUTHOR_PATH.split(","));
        web.ignoring().antMatchers("/error");
    }


    /**
     * 权限配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http
                        //禁用CSRF
                        .csrf().disable()
                        .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                        //授权异常
                        .exceptionHandling()
                        // 未登录认证异常
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        // 登录过后访问无权限的接口时自定义403响应内容
                        .accessDeniedHandler(new URLAccessDeniedHandler())
                        //防止iframe 造成跨域
                        .and().headers().frameOptions().disable()
                        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//禁用session
                        //授权资源
                        .and().authorizeRequests();
        // url权限认证处理
//        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//            @Override
//            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                o.setAccessDecisionManager(new SecurityAuthenticationHandle());
//                return o;
//            }
//        });
        // 其余所有请求都需要认证
        registry.anyRequest().authenticated();
        registry.and().apply(new AuthorConfigurer(authorSecurityHandle));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new SecurityPasswordEncoder();
    }

    /**
     * <p> 全局配置解决跨域 </p>
     *
     * @author : 芙蓉王
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "HEAD", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}

