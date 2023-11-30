package com.e107.backend.geChu.security.config;

import com.e107.backend.geChu.security.jwt.JwtAccessDeniedHandler;
import com.e107.backend.geChu.security.jwt.JwtAuthenticationEntryPoint;
import com.e107.backend.geChu.security.jwt.JwtSecurityConfig;
import com.e107.backend.geChu.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)    //!! @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 추가
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    //!! tokenProvider, corsFilter, jwtAuthenticationEntryPoint, jwtAccessDeniedHandler 를 생성자로 주입받는다.
    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        logger.debug("-SecurityConfig(\n" +
                "            TokenProvider tokenProvider,\n" +
                "            CorsFilter corsFilter,\n" +
                "            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,\n" +
                "            JwtAccessDeniedHandler jwtAccessDeniedHandler\n" +
                "    )");
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("-passwordEncoder()");return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        logger.debug("-filterChain(HttpSecurity httpSecurity)");
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                //!! 우리가 만든 예외 핸들러를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/**").permitAll()
                //.requestMatchers(/*PathRequest.toH2Console()*/ new AntPathRequestMatcher("/")).permitAll()
                .antMatchers("/user/**").authenticated()	//!! 특정 링크로 들어오면 인증이 필요
                //.antMatchers("/api/**").permitAll()
                //.requestMatchers( new AntPathRequestMatcher("/api/hello"), ("/api/authenticate", "/api/signup")).permitAll()  //!! 로그인API, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 모두 permitAll() 처리
                //.requestMatchers(/*PathRequest.toH2Console()*/ new AntPathRequestMatcher("/")).permitAll()

                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));   //!! JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig를 SecurityConfig에 적용

        return httpSecurity.build();
    }


}