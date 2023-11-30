package com.e107.backend.geChu.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//!! 필용한 권한이 존재하지 않는 경웅 403 Forbidden 에러를 리턴하는 클래스
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      //필요한 권한이 없이 접근하려 할때 403
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
   }
}
