package com.e107.backend.geChu.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
//!! jwt를 위한 커스텀한 필터
public class JwtFilter extends GenericFilterBean {

   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
   public static final String AUTHORIZATION_HEADER = "Authorization";
   private TokenProvider tokenProvider;

   // !! JwtFilter 는 TokenProvider 를 주입받아서 사용
   public JwtFilter(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   // !! jwt 토큰의 인증 정보를 현재 실행중인 SecurityContext 에 저장하는 역할을 함
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      logger.debug("-doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)");
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      String jwt = resolveToken(httpServletRequest);  //!! 토큰 정보를 꺼내오는 메소드
      String requestURI = httpServletRequest.getRequestURI();

      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  //!! 토큰 정보가 있고 유효한 토큰인지 검증
         Authentication authentication = tokenProvider.getAuthentication(jwt);   //!! 토큰이 정상적이면 토큰으로부터 Authentication 객체를 만들어서
         SecurityContextHolder.getContext().setAuthentication(authentication);   //!! SecurityContext 에 저장
         logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
      } else {
         logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   //!! 필터링을 하기위해 토큰 정보가 있어야 하기때문에 (request header에서)토큰 정보를 꺼내오는 메소드
   private String resolveToken(HttpServletRequest request) {
      logger.debug("-resolveToken(HttpServletRequest request)");
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }

      return null;
   }
}
