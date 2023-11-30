package com.e107.backend.geChu.dto.response;

import com.e107.backend.geChu.domain.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NewsRespDto {
    Long id;
    String subject;
    String url;
    String imageLink;
    String date;

    public static NewsRespDto of(News n) {
        return NewsRespDto.builder()
                .id(n.getId())
                .subject(n.getSubject())
                .url(n.getUrl())
                .imageLink(n.getImageLink())
                .date(n.getDate())
                .build();
    }

}
