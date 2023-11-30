package com.e107.backend.geChu.dto.response;

import com.e107.backend.geChu.domain.entity.Friend;
import com.e107.backend.geChu.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FriendRespDto {

    Long id;
    String name;

    public static FriendRespDto of(Friend f) {
        return FriendRespDto.builder()
                .id(f.getId())
                .name(f.getName())
                .build();
    }
}
