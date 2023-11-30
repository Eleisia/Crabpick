package com.e107.backend.geChu.dto.response;

import com.e107.backend.geChu.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentRespDto {

    private Long id;
    private Long gameId;
    private Long memberId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;
    private Long myScore;
    private String headerImg;

    public static CommentRespDto of(Comment c) {
        return CommentRespDto.builder()
                .id(c.getId())
                .gameId(c.getGame().getAppId())
                .memberId(c.getMember().getId())
                .memberName(c.getMember().getNickname())
                .content(c.getContent())
                .headerImg("https://cdn.cloudflare.steamstatic.com/steam/apps/" + c.getGame().getAppId() +"/header.jpg")
                .createdAt(c.getCreatedAt())
                .build();
    }
}
