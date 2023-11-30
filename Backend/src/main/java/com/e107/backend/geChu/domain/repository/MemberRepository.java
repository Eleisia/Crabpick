package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.dto.response.CommentRespDto;
import com.e107.backend.geChu.dto.response.GameOwnedMemberRespDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select new com.e107.backend.geChu.dto.response.GameOwnedMemberRespDto(f.id, f.name, f.member.id, s.member.username, s.gameId)" +
            " from Member m join m.friends f join m.steamLibraries s where s.gameId = :gameId and m.id = :memberId")
    List<GameOwnedMemberRespDto> findOwnedFriend(@Param("memberId") Long memberId, @Param("gameId") Long gameId);


    //!! @EntityGraph은 쿼리가 수행 될때 Lazy조회가 아닌 Eager 조회로 authorities정보를 같이 가져오게 된다.
    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByUsername(String username);

    Member findByusername(String username);

}