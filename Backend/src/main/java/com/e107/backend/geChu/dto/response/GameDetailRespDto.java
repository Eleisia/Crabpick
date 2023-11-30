package com.e107.backend.geChu.dto.response;

import com.e107.backend.geChu.domain.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GameDetailRespDto {

    private Long id;
    private Long appId;
    private Long ageLimit;
    private String name;
    private String developer;
    private String genre;
    private String release;
    private Float avgPlaytime;
    private String mood;
    private String wordCloud;
    private String steamLink;
    private String imgUrl;
    private String trailer_url;
    private List<CommentRespDto> comments;

    public static GameDetailRespDto of(Game g) {
        return GameDetailRespDto.builder()
                .id(g.getId())
                .genre(g.getGenre())
                .appId(g.getAppId())
                .name(g.getName())
                .release(g.getReleaseDate())
                .avgPlaytime(g.getAvgPlaytime())
                .mood(g.getMood())
                .ageLimit(g.getAgeLimit())
                .developer(g.getDevelopers())
                .wordCloud(g.getWordCloud())
                .steamLink(g.getSteamLink())
                .trailer_url(g.getTrailerLink())
                .imgUrl(g.getImageLink())
                .build();
    }

    public void setComments(List<CommentRespDto> list) {
        this.comments = list;
    }
}
