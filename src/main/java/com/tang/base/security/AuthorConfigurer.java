package com.tang.base.security;

import com.tang.base.filter.SecurityAuthorFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 
 * @ClassName AuthorConfigurer
 * @Description 授权
 * @author 芙蓉王
 * @Date Mar 12, 2020 8:59:49 PM
 * @version 1.0.0
 */
public class AuthorConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthorSecurityHandle authorSecurityHandle;
    
    public AuthorConfigurer(AuthorSecurityHandle authorSecurityHandle) {
        super();
        this.authorSecurityHandle = authorSecurityHandle;
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        SecurityAuthorFilter authorFilter = new SecurityAuthorFilter(authorSecurityHandle);
        http.addFilterBefore(authorFilter, UsernamePasswordAuthenticationFilter.class);
    }

    
}
