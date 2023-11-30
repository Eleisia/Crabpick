package com.e107.backend.geChu.controller;

import com.e107.backend.geChu.dto.request.MyGameReqDto;
import com.e107.backend.geChu.dto.response.*;
import com.e107.backend.geChu.service.CommentService;
import com.e107.backend.geChu.service.GameService;
import com.e107.backend.geChu.service.MemberService;
import com.e107.backend.geChu.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GameController {

    private final GameService gameService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final SellerService sellerService;


    @GetMapping
    public ResponseEntity<List<GameListRespDto>> getGameList(Pageable pageable) {
     return new ResponseEntity<>(gameService.findAllGame(pageable), OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Map<String, Object>> getGameByName(@PathVariable String name, Pageable pageable) {
        return new ResponseEntity<>(gameService.findGameByName(name,pageable), OK);
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopSellerRespDto>> getTopGameList() {
        return new ResponseEntity<>(sellerService.findAllTopSeller(), OK);
    }

    @GetMapping("/discount")
    public ResponseEntity<List<DiscountRespDto>> getDiscountList() {
        return new ResponseEntity<>(sellerService.findAllDiscount(), OK);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDetailRespDto> getGame(@PathVariable Long gameId) {
        GameDetailRespDto dto = gameService.findGameByAppId(gameId);
        List<CommentRespDto> l = commentService.findCommentByGameId(gameId);
        dto.setComments(l);
        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping("/recommend/{gameId}")
    public ResponseEntity<List<GameListRespDto>> getRecommendGameList(@PathVariable Long gameId) {
        return new ResponseEntity<>(gameService.findRecommendByGame(gameId), OK);
    }

    @GetMapping("/recommend/user/{userId}")
    public ResponseEntity<Map<String, List<GameListRespDto>>> getUserRecommendList(@PathVariable Long userId) {

        return new ResponseEntity<>(gameService.findRecommendByUser(userId), OK);
    }

}
