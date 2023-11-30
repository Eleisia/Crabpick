package com.e107.backend.geChu.dto.response;

import com.e107.backend.geChu.domain.entity.SteamLibrary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SteamLibraryRespDto {

    private Long gameId;
    private String name;
    private Float playTime;
    private Long steamReviewScore;
    private Long myScore;
    private String imgUrl;

    public static SteamLibraryRespDto of(SteamLibrary s) {
        return SteamLibraryRespDto.builder()
                .gameId(s.getGameId())
                .name(s.getName())
                .playTime(s.getPlayTime())
                .steamReviewScore(s.getSteamReviewScore())
                .myScore(s.getMyScore())
                .imgUrl(s.getImgUrl())
                .build();
    }
}
