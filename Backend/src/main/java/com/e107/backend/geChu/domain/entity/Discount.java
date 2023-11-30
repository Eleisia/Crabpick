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
public class Discount {
    @Id
    @GeneratedValue
    private Long id;
    @org.hibernate.annotations.Comment("게임 아이디")
    private Long gameId;
    @org.hibernate.annotations.Comment("게임이름")
    private String name;
    @org.hibernate.annotations.Comment("할인율")
    private Long discountPercent;
    @org.hibernate.annotations.Comment("원가")
    private Long originalPrice;
    @org.hibernate.annotations.Comment("최종가")
    private Long finalPrice;
    @Comment("이미지링크")
    private String imageLink;
}
