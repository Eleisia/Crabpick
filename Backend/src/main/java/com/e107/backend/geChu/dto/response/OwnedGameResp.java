package com.e107.backend.geChu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OwnedGameResp {

    Long appId;
    String name;
    String headerImg;
    Long playTime;

    public static OwnedGameResp of(Long appId, String name, Long playTime) {
        return OwnedGameResp.builder()
                .appId(appId)
                .name(name)
                .headerImg("https://cdn.cloudflare.steamstatic.com/steam/apps/" + appId +"/header.jpg")
                .playTime(playTime)
                .build();
    }
}
