package com.e107.backend.geChu.domain.entity;


import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Similarity {

    @Id
    private Long id;

    @Comment("유사도")
    @Column(columnDefinition = "TEXT")
    private String similarity;
}
