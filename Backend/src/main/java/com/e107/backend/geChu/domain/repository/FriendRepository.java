package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.Friend;
import com.e107.backend.geChu.dto.response.GameOwnedMemberRespDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByMemberId(Long memberId);

    //TODO 나중에 수정필요
    @Query(value = "select new com.e107.backend.geChu.dto.response.GameOwnedMemberRespDto(f.id, f.name, f.member.id, s.member.username, s.gameId) " +
            "from Friend f join  f.member m  JOIN m.steamLibraries s where s.gameId = :gameId and m.id = :memberId and " +
            "EXISTS (select 1 FROM SteamLibrary s2 WHERE s2.member.id = f.id AND s2.gameId = :gameId)")
    List<GameOwnedMemberRespDto> findOwnedFriend(@Param("memberId") Long memberId, @Param("gameId") Long gameId);
}
