package com.e107.backend.geChu.security.controller;

import com.e107.backend.geChu.domain.repository.MemberRepository;
import com.e107.backend.geChu.security.dto.LoginDto;
import com.e107.backend.geChu.security.dto.TokenDto;
import com.e107.backend.geChu.security.jwt.JwtFilter;
import com.e107.backend.geChu.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MemberRepository memberRepository;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.memberRepository = memberRepository;
    }

    //!! 로그인
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        logger.info("-authorize(@Valid @RequestBody LoginDto loginDto)");

        //!! LoginDto의 username, password를 파라미터로 받고 이를 이용해 UsernamePasswordAuthenticationToken을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //!! authenticate() 메서드를 호출하면 CustomUserDetailsService의 loadUserByUsername() 메서드가 호출된다. -> 결과를 authentication 객체를 만들고 SecurityContext에 저장
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //!! JWT 토큰을 생성한다.
        String jwt = tokenProvider.createToken(authentication);
        System.out.println("토큰 생성 완료 : " + jwt);

        //!! 토큰을 헤더에 담고,
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        Long memberId = memberRepository.findByusername(loginDto.getUsername()).getId();

        //!! TokenDto을 이용해 response body에 넣어 반환
        return new ResponseEntity<>(new TokenDto(jwt, memberId), httpHeaders, HttpStatus.OK);
    }
}
