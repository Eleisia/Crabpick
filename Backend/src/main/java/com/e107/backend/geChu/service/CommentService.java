package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.Comment;
import com.e107.backend.geChu.dto.response.CommentRespDto;

import java.util.List;

public interface CommentService {
     List<CommentRespDto> findCommentByGameId(Long gameId);

    List<CommentRespDto> findCommentByMemberId(Long memberId);

    boolean saveComment(Long memberId, Long gameId, String content);

    boolean deleteComment(Long commentId);

}
