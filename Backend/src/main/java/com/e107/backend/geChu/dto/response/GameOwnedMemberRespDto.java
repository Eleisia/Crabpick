package com.e107.backend.geChu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GameOwnedMemberRespDto {
    Long id;
    String name;
    Long memberId;
    String memberName;
    Long gameId;
}
