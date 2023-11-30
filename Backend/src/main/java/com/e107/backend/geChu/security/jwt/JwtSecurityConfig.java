package com.e107.backend.geChu.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//!! 직접 만든 TokenProvider 와 JwtFilter 를 SecurityConfig 에 적용할 때 사용
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private static final Logger logger = LoggerFactory.getLogger(JwtSecurityConfig.class);
    private TokenProvider tokenProvider;

    //!! TokenProvider 를 주입
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        logger.debug("-JwtSecurityConfig(TokenProvider tokenProvider)");this.tokenProvider = tokenProvider;
    }

    //!!  JwtFilter 를 Security 로직에 필터를 등록
    @Override
    public void configure(HttpSecurity http) {
        logger.debug("-configure(HttpSecurity http)");
        http.addFilterBefore(
            new JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );
    }
}
