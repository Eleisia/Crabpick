package com.e107.backend.geChu.controller;

import com.e107.backend.geChu.domain.entity.Friend;
import com.e107.backend.geChu.domain.entity.Member;
import com.e107.backend.geChu.domain.repository.MemberRepository;
import com.e107.backend.geChu.dto.request.MemberAddReqDto;
import com.e107.backend.geChu.dto.response.*;
import com.e107.backend.geChu.service.CommentService;
import com.e107.backend.geChu.service.GameService;
import com.e107.backend.geChu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final MemberService memberService;
    private final GameService gameService;
    private final CommentService commentService;

    // ## 사용 안함
    @GetMapping
    public ResponseEntity<List<MemberRespDto>> getAllMember() {
        List<MemberRespDto> list = memberService.findAllMember();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//## userservice에서 옮기기
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberRespDto> getMember(@PathVariable Long memberId) {
        MemberRespDto dto = memberService.findMemberById(memberId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @GetMapping("/comment/{memberId}")
    public ResponseEntity<List<CommentRespDto>> getComment(@PathVariable Long memberId) {
        List<CommentRespDto> dto = commentService.findCommentByMemberId(memberId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @GetMapping("/{memberId}/game")
    public ResponseEntity<List<OwnedGameResp>> getOwnedGame(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.findOwnedGame(memberId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody MemberAddReqDto dto) {
        memberService.saveMember(dto.toEntity());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    //찜 관련
    //게임 찜 목록 추가
    @PostMapping("bookmark/{memberId}/{gameId}")
    public ResponseEntity<String> addBookmark(@PathVariable Long memberId, @PathVariable Long gameId) {
        memberService.addBookmark(memberId, gameId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    //게임 찜 목록 삭제
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(@PathVariable Long bookmarkId) {
        memberService.deleteBookmark(bookmarkId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    //멤버의 게임 찜 조회
    @GetMapping("/bookmark/{memberId}")
    public ResponseEntity<List<BookmarkRespDto>> getBookmarkList(@PathVariable Long memberId){
        return new ResponseEntity<>(memberService.findAllBookmark(memberId), HttpStatus.OK);
    }

    //해당 멤버의 친구 목록 반환
    @GetMapping("/friend/{memberId}")
    public ResponseEntity<Object> getFriendList(@PathVariable Long memberId){
        return new ResponseEntity<>(memberService.findAllFriend(memberId), HttpStatus.OK);
    }
}
