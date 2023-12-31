package com.e107.backend.geChu.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
//!!토큰 유효성 검증 등을 담당
@Component
public class TokenProvider implements InitializingBean {


   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long tokenValidityInMilliseconds;
   private Key key;

   public TokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
      logger.debug("-TokenProvider(String secret, long tokenValidityInSeconds)");
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
   }

   //!! 빈이 생성이 되고 주입을 받은 후에 secret값을 base64 Decode해서 key변수에 할당
   @Override
   public void afterPropertiesSet() {
      logger.debug("-afterPropertiesSet()");
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   // !! Authentication객체의 권한정보를 이용해서 토큰을 생성
   public String createToken(Authentication authentication) {
      logger.debug("-createToken(Authentication authentication)");
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));
      logger.debug("###createToken(Authentication authentication) " + authorities.toString());

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds); //!! application.yml에서 설정했던 만료시간을 설정하고

      //!! 토큰을 생성
      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim(AUTHORITIES_KEY, authorities)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }

   ///////////////////////////////////////////////////////////////////////////////////////////////
   // !! 토큰을 파라미터로 받아서 토큰의 권한 정보로 Authentication 객체를 리턴
   public Authentication getAuthentication(String token) {
      logger.debug("-getAuthentication(String token)");
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      //!! claim에서 권한 정보를 빼온다
      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      //!! 권한정보(authorities)를 이용해서 User 객체를 생성
      User principal = new User(claims.getSubject(), "", authorities);

      //!! 토큰에 담긴 정보를 기반으로 Authentication 객체를 리턴
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   // !! 토큰의 유효성을 검증
   public boolean validateToken(String token) {
        logger.debug("-validateToken(String token)");
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         logger.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         logger.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }
}
