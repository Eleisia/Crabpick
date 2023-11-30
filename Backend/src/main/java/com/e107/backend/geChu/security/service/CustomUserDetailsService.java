package com.e107.backend.geChu.security.service;

import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.domain.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

   private final MemberRepository memberRepository;

   //!! UserRepository를 주입받는다.
   public CustomUserDetailsService( MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      logger.debug("-loadUserByUsername(final String username)");
      //!! 로그인 시 DB에서 유저정보, 권한정보를 가져오게 된다.-> 해당 정보를 기반으로 userdetails.User를 생성하여 반환한다.(아래 코드 내용 포함)
      return memberRepository.findOneWithAuthoritiesByUsername(username)
         .map(member -> createUser(username, member))
         .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, Member member) {
      logger.debug("-createUser(String username, User user)");
      if (!member.isActivated()) {
         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
      }

      List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(member.getUsername(),
              member.getPassword(),
              grantedAuthorities);
   }
}
