package com.e107.backend.geChu.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SteamLibrary {
    @Id
    @GeneratedValue
    private Long id;

    @Comment("게임 아이디")
    private Long gameId;
    @Comment("게임 이름")
    private String name;
    @Comment("플레이시간")
    private Float playTime;
    @Comment("스팀리뷰점수")
    private Long steamReviewScore;
    @Comment("나의 점수")
    private Long myScore;
    @Comment("이미지 주소")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Member member;

}
