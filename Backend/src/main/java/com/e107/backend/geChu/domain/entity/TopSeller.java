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
public class TopSeller {

    @Id
    @GeneratedValue
    private Long id;
    @Comment("게임 아이디")
    private Long gameId;
    @Comment("게임이름")
    private String name;
    @Comment("할인율")
    private Long discountPercent;
    @Comment("원가")
    private Long originalPrice;
    @Comment("최종가")
    private Long finalPrice;
    @Comment("이미지링크")
    private String imageLink;
}
