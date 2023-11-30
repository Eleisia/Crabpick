package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.Friend;
import com.e107.backend.geChu.domain.entity.Game;
import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.domain.entity.SteamLibrary;
import com.e107.backend.geChu.domain.repository.FriendRepository;
import com.e107.backend.geChu.domain.repository.GameRepository;
import com.e107.backend.geChu.domain.repository.MemberRepository;
import com.e107.backend.geChu.domain.repository.SteamLibraryRepository;
import com.e107.backend.geChu.domain.entity.*;
import com.e107.backend.geChu.domain.repository.*;
import com.e107.backend.geChu.dto.response.*;
import com.e107.backend.global.common.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final FriendRepository friendRepository;

    private final BookmarkRepository bookmarkRepository;
    private final SteamLibraryRepository steamLibraryRepository;

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);

    }

    @Override
    public List<MemberRespDto> findAllMember() {
        List<MemberRespDto> respList = new ArrayList<>();
        List<Member> list = memberRepository.findAll();
        for (Member m : list) {
            log.info("MEMBER_INFO=========================================================");
            respList.add(MemberRespDto.of(m));
        }
        return respList;
    }

    @Override
    public MemberRespDto findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(2, "Member객체가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR));
        MemberRespDto dto = MemberRespDto.of(member);
        List<Friend> flist = friendRepository.findByMemberId(memberId);
        List<FriendRespDto> respList = new ArrayList<>();
        for (Friend f : flist) {
            log.info("FRIEND_INFO=========================================================");
            respList.add(FriendRespDto.of(f));
        }
        dto.setFriend(respList);
        return dto;
    }


    @Override
    public List<OwnedGameResp> findOwnedGame(Long memberId) {
        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=${STEAM_KEY}&steamid="
                + memberId + "&format=json&include_played_free_games=1";
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(header);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
        ResponseEntity<?> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        org.json.JSONObject jo = new org.json.JSONObject(resultMap.getBody().toString());
        org.json.JSONObject jo2 = (org.json.JSONObject) jo.get("response");
        log.info(resultMap.getStatusCode().toString());
        log.info(jo.toString());
        log.info(jo2.toString());
        if(!jo2.has("games")) return null;
        org.json.JSONArray jo3 = (org.json.JSONArray) jo2.get("games");
        List<OwnedGameResp> l = new ArrayList<>();
        for (Object j : jo3) {
            Game game = gameRepository.findByAppId(Long.parseLong(((org.json.JSONObject) j).get("appid").toString()));
            Long playtime = Long.parseLong(((org.json.JSONObject) j).get("playtime_forever").toString()) / 60;
            if (game == null) continue;
            l.add(OwnedGameResp.of(game.getAppId(),game.getName(),playtime));
        }
        l.sort(Comparator.comparing(OwnedGameResp::getPlayTime).reversed());
        return l;
    }

    //찜 게임 추가
    @Override
    public void addBookmark(Long memberId, Long gameId){

        Member memberEntity = memberRepository.findById(memberId).get();
        Optional<Game> optionalGameEntity = gameRepository.findById(gameId);

        if(optionalGameEntity.isEmpty()){
            throw new CommonException(2, "게임이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //존재하는 게임 확인
        for(Bookmark b : memberEntity.getBookmarks()){
            if(b.getGame().getId().equals(gameId)){
                throw new CommonException(2, "이미 찜한 게임입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        bookmarkRepository.save(Bookmark.builder()
                .member(memberEntity)
                .game(optionalGameEntity.get())//엔티티에 저장
                .build());
    }

    // 게임 찜 삭제
    @Override
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    @Override
    public List<BookmarkRespDto> findAllBookmark(Long memberId) {
        Member memberEntity = memberRepository.findById(memberId).get();
        List<BookmarkRespDto> bookmarkRespDtoList = new ArrayList<>();
        for(Bookmark b : memberEntity.getBookmarks()){
            BookmarkRespDto dto = BookmarkRespDto.builder()
                    .id(b.getId())
                    .gameId(b.getGame().getId())
                    .name(b.getGame().getName())
                    .headerImg("https://cdn.cloudflare.steamstatic.com/steam/apps/" + b.getGame().getAppId() +"/header.jpg")
                    .build();
            bookmarkRespDtoList.add(dto);
        }
        return bookmarkRespDtoList;
    }

    @Override
    public Object findAllFriend(Long memberId) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id=" + memberId));
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.steampowered.com/ISteamUser/GetFriendList/v1?key=${STEAM_KEY}&steamid=" + memberEntity.getUsername();

        Object o = restTemplate.getForObject(url, Object.class);

        return o;
    }


}
