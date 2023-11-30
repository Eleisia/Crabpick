package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.Comment;
import com.e107.backend.geChu.domain.entity.Game;
import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.domain.repository.CommentRepository;
import com.e107.backend.geChu.domain.repository.GameRepository;
import com.e107.backend.geChu.domain.repository.MemberRepository;
import com.e107.backend.geChu.dto.response.CommentRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    @Override
    public List<CommentRespDto> findCommentByGameId(Long gameId) {
        List<CommentRespDto> list = new ArrayList<>();
        Game game = gameRepository.findByAppId(gameId);
        List<Comment> resp = commentRepository.findByGameId(game.getId());
        for (Comment c : resp) {
            log.info(c.getContent());
            list.add(CommentRespDto.of(c));
        }
        return list;
    }

    @Override
    public List<CommentRespDto> findCommentByMemberId(Long memberId) {
        List<CommentRespDto> list = new ArrayList<>();
        List<Comment> resp = commentRepository.findByMemberId(memberId);
        for (Comment c : resp) {
            log.info(c.getContent());
            list.add(CommentRespDto.of(c));
        }
        return list;
    }

    @Override
    public boolean saveComment(Long memberId, Long gameId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member id"));
        Game game = gameRepository.findByAppId(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Invalid game id");
        }
        Comment comment = Comment.builder()
                .member(member)
                .game(game)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        return true;
    }

    @Override
    public boolean deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return true;
    }
}