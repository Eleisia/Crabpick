package com.e107.backend.geChu.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Comment("작성자 아이디")
    private Long authorId;
    @Comment("언어")
    private String language;
    @Comment("내용")
    private String content;
    @Comment("리뷰작성시 플레이시간")
    private Float playtimeAtReview;
    @Comment("총 플레이시간")
    private Float playtimeAll;
    @Comment("게임 추천여부")
    private Boolean isRecommend;
    @Comment("리뷰 추천수")
    private Long votesUp;
}
