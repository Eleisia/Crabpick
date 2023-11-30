package com.e107.backend.geChu.dto.response;


import com.e107.backend.geChu.domain.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GameListRespDto {
    Long id;
    Long appId;
    String name;
    String imgUrl;
    String headerImg;
    String genre;


    public static GameListRespDto of(Game g) {
        return GameListRespDto.builder()
                .id(g.getId())
                .name(g.getName())
                .appId(g.getAppId())
                .imgUrl(g.getImageLink())
                .headerImg("https://cdn.cloudflare.steamstatic.com/steam/apps/" + g.getAppId() +"/header.jpg")
                .genre(g.getGenre())
                .build();
    }
}
