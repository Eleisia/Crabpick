package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByMemberIdAndGameId(Long memberId, Long gameId);
    List<Comment> findByGameId(Long gameId);
    List<Comment> findByMemberId(Long memberId);
}
