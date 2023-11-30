package com.e107.backend.geChu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookmarkRespDto {

    private Long id;

    private Long gameId;

    private String name;
    private String headerImg;
}
