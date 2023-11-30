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
public class News{
    @Id
    @GeneratedValue
    private Long id;

    @Comment("제목")
    private String subject;

    @Comment("링크")
    private String url;

    @Comment("이미지링크")
    private String imageLink;

    @Comment("작성일")
    private String date;

    public void setAttribute(String subject, String url, String imageLink, String date){
        this.subject = subject;
        this.url = url;
        this.imageLink = imageLink;
        this.date = date;
    }
}