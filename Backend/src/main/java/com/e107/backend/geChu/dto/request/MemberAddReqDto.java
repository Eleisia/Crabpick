package com.e107.backend.geChu.dto.request;

import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.domain.entity.Friend;
import lombok.*;

import java.util.HashSet;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberAddReqDto {

    private String username;
    private String nickname;

    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .nickname(this.nickname)
                .build();
    }
}
