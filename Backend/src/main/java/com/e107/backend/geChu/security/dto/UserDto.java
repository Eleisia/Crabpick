package com.e107.backend.geChu.security.dto;

import com.e107.backend.geChu.domain.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

   @NotNull
   @Size(min = 3, max = 50)
   private String username;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @NotNull
   @Size(min = 3, max = 100)
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   private Set<AuthorityDto> authorityDtoSet;

   //////////////////////////////////////////park
   public static UserDto from(Member member) {
      if(member == null) return null;

      return UserDto.builder()
              .id(member.getId())
              .username(member.getUsername())
              .nickname(member.getNickname())

              .authorityDtoSet(member.getAuthorities().stream()
                      .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                      .collect(Collectors.toSet()))
              .build();
   }
}