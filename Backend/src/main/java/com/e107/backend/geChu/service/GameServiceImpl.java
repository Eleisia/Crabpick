package com.e107.backend.geChu.service;

import com.e107.backend.geChu.domain.entity.Game;
import com.e107.backend.geChu.domain.entity.Similarity;
import com.e107.backend.geChu.domain.repository.GameRepository;
import com.e107.backend.geChu.domain.repository.SimilarityRepository;
import com.e107.backend.geChu.dto.request.MyGameReqDto;
import com.e107.backend.geChu.dto.response.GameDetailRespDto;
import com.e107.backend.geChu.dto.response.GameListRespDto;
import com.e107.backend.geChu.dto.response.OwnedGameResp;
import com.e107.backend.global.common.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final SimilarityRepository similarityRepository;
    private final MemberService memberService;

    @Override
    public GameDetailRespDto findGameByAppId(Long gameId) {
        Game game = gameRepository.findByAppId(gameId);
        if (game == null) {
            throw new CommonException(2, "Game객체가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return GameDetailRespDto.of(game);
    }


    @Override
    public List<GameListRespDto> findAllGame(Pageable pageable) {
        return gameRepository.findAll(pageable).stream().map(GameListRespDto::of).collect(Collectors.toList());
    }

    @Override

    public Map<String, Object> findGameByName(String name, Pageable pageable) {
        Page<Game> p = gameRepository.findByNameContaining(name, pageable);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("pages", p.getTotalPages());
        map.put("data", p.stream().map(GameListRespDto::of).collect(Collectors.toList()));
        return map;
    }

    @Override
    public List<GameListRespDto> findRecommendByGame(Long gameId) {
        Similarity s = similarityRepository.findById(gameId)
                .orElseThrow(() -> new CommonException(2, "Game객체가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR));
        String similarity = s.getSimilarity();
        String[] arr = similarity.split(" ");
        ArrayList<GameListRespDto> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            String[] item = arr[i].split(":");
            Long id = Long.parseLong(item[0]);
            Game g = gameRepository.findByAppId(id);
            if (g == null) continue;
            list.add(GameListRespDto.of(g));

        }
        log.info("similarity : " + similarity);

        return list;
    }

    @Override
    public Map<String, List<GameListRespDto>> findRecommendByUser(Long userId) {  //유저가 보유한 게임을 조회하기 위해 유저아이디를 받는다
        //# {(유져 보유 게임 id) : {playtime: 5, data: {1:3.2, 2:11, 3:45}}, (유져 보유 게임 id) : {playtime: (플레이시간) 형태의 딕셔너리 생각중}
        List<OwnedGameResp> ownedGame = memberService.findOwnedGame(userId); //유저가 보유한 게임을 조회한다
        if (ownedGame == null) return null; //유저가 보유한 게임이 없다면 null을 반환한다
        LinkedHashMap<Long, OwnedGameResp> dto = new LinkedHashMap<>(); //추후에 추천할 게임을 담을떄 보유중 게임을 제외하기 위해 사용한다
        for (OwnedGameResp game : ownedGame) { //보유중인 게임을 dto에 담는다
            dto.put(game.getAppId(), game );
        }
        Map<Long, Map<String, Object>> userGame = new LinkedHashMap<>(); //유저가 보유한 게임을 담을 맵
        for (OwnedGameResp d : dto.values()) { //유저가 보유한 게임을 담는다
            Optional<Similarity> byId = similarityRepository.findById(d.getAppId()); //유저가 보유한 게임의 유사도를 조회한다
            Similarity s = byId.orElse(null); //유저가 보유한 게임의 유사도가 없다면 null을 반환한다
            if (s == null) continue; //유저가 보유한 게임의 유사도가 없다면 다음 게임으로 넘어간다
            String similarity = s.getSimilarity(); //유저가 보유한 게임의 유사도를 가져온다
            String[] arr = similarity.split(" "); //유사도를 공백을 기준으로 나눈다
            Map<Long, Double> data = new LinkedHashMap<>(); //유사도를 담을 맵
            for (int i = 0; i < arr.length; i++) { //유사도를 담는다
                String[] item = arr[i].split(":"); //유사도를 :를 기준으로 나눈다
                Long id = Long.parseLong(item[0]); //유사도의 id를 가져온다
                Double val = Double.parseDouble(item[1]); //유사도의 값(유사도)를 가져온다
                data.put(id, val); //유사도를 담는다
            }
            Map<String, Object> map = new LinkedHashMap<>(); //유저가 보유한 게임의 플레이시간과 유사도를 담을 맵
            map.put("playtime", d.getPlayTime()); //유저가 보유한 게임의 플레이시간을 담는다
            map.put("data", data); //유사도를 담는다

            userGame.put(d.getAppId(), map); //유저가 보유한 게임의 플레이시간과 유사도를 담는다
        }

        List<GameListRespDto> m = listOfRecommend(userGame, dto); //유사도 알고리즘을 계산하기 위한 메소드
        Map<String, List<GameListRespDto>> result = new LinkedHashMap<>();
        result.put("data", m);
        return result;
    }


    @Override
    public List<GameListRespDto> listOfRecommend(Map<Long, Map<String, Object>> map, Map<Long,OwnedGameResp> dto) { //유사도 알고리즘을 계산하기 위한 메소드
        Map<Long, Double> totalDict = new LinkedHashMap<>(); //유사도를 담을 맵
        for (Map<String, Object> m : map.values()) { //유사도를 담는다
            Long playtime = (Long)m.get("playtime"); //유저가 보유한 게임의 플레이시간을 가져온다
            if (playtime < 2) continue; //유저가 보유한 게임의 플레이시간이 2시간 미만이라면 다음 게임으로 넘어간다
            int playtimeFactor = calcPlaytimeFactor(playtime); //유저가 보유한 게임의 플레이시간에 따라 가중치를 부여한다
            Map<Long, Double> data = (Map<Long, Double>) m.get("data"); //유사도를 가져온다
            for (Map.Entry<Long, Double> entry : data.entrySet()) { //유사도를 담는다
                Long key = entry.getKey();
                Double value = entry.getValue();
                if (totalDict.containsKey(key)) { //이미 유사도가 담겨있다면 값을 더함
                    totalDict.put(key, totalDict.get(key) + value * playtimeFactor);
                } else {
                    totalDict.put(key, value * playtimeFactor); //유사도가 담겨있지 않다면 값을 담음
                }
            }
        }
        List<Map.Entry<Long, Double>> e = new LinkedList<>(totalDict.entrySet()); //유사도를 정렬하기 위해 리스트로 변환
        e.sort((o1, o2) -> (int) (o2.getValue() - o1.getValue())); //유사도를 내림차순으로 정렬
        int i = 0;
        ArrayList<GameListRespDto> list = new ArrayList<>();
        for(Map.Entry<Long, Double> entry : e){ //유사도를 담는다
            if (i == 30) break; //유사도가 높은 30개의 게임만 추천
            if (dto.containsKey(entry.getKey())) continue; //유저가 보유한게임 제외
//            System.out.println("KEY==================");
//            System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
            Game g = gameRepository.findByAppId(entry.getKey());
            if (g == null) continue;
            list.add(GameListRespDto.of(g));
            i++;
        }
        return list;
    }

    @Override
    public int calcPlaytimeFactor(Long playtime) { //유저가 보유한 게임의 플레이시간에 따라 가중치를 부여한다
        if (playtime < 200) return 1; // 200시간 미만이라면 1
        return (int) (log2(playtime / 6000.0f)) + 1; // 200시간 이상이라면 로그를 취한 값에 1을 더함
    }

    @Override
    public double log2(float x) {
        return Math.log(x) / Math.log(2);
    } //로그2를 구하는 메소드
}



