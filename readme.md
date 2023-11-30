# steam 게임 추천 서비스(빅데이터 분산)

## 목차

1. 서비스 소개
2. 기획 배경
3. 기능 소개
4. 기술 스택
5. 일정 (간트 차트)
6. 그라운드 룰
7. 코드 컨벤션

## 1. 서비스 소개

### 개요

- **소개**: 스팀 게임 댓글 기반 게임 추천 및 게임 종합 커뮤니티
- **서비스 명**: CrabPICK!



### 타겟🎯

- 다양한 게임을 즐기는 게이머
- 매니악한 취향을 가진 게이머


## 2. 기획 배경

평소 다양한 게임을 경험해보고 싶어서 스팀에서 돈을 주고 게임을 구매 하지만 나와 맞지 않거나 게임을 고르다가 시간을 다 낭비해버리는 사람들이 많다. 이러한 문제점을 해결하기 위해 실제 게임을 이용한 유저들의 댓글을 분석하고 나와 맞는 게임을 추천해주는 서비스를 기획하게 되었다.



## 3. 기능소개

### 아이디어

- 약 30,000,000여개의 리뷰 데이터를 활용해서 유저간 유사도를 분석하고, 이를 이용한 게임을 추천합니다.

### 세부 내역

#### 데이터 분류

- 빅데이터 분산기술인 Hadoop을 활용하여 전체 리뷰데이터 내의 작성자 정보를 이용하여 1차적으로 유저들의 게임보유목록을 만듭니다.
- 생성된 유저 게임보유목록을 토대로 유저간 유사도를 활용해서 게임 별로 추천리스트를 생성합니다.
- 회원의 라이브러리와 게임 별 추천리스트를 활용해서 적절한 게임을 추천합니다.


#### 데이터 추천

- 데이터 분류를 통해 추출한 데이터와 유저 정보, 가격 정보 등을 기반으로 새로운 추천 리스트를 생성하여 사용자에게 제공합니다.
  - 리뷰를 작성한 사용자들의 구매 이력을 조회하여 연관된 게임을 추천합니다.
  - 리뷰를 분석하여 현재 이 게임의 평균 플레이타임을 계산합니다.

### 참고 사이트

- Steam API 명세사이트
  
    [Documentation Home Page (Steamworks Documentation)](https://partner.steamgames.com/doc/home)
    
- 댓글 데이터 예시(Counter-Strike Global Offensive 를 이용한 예시)

```markdown
{
  "success": 1,
  "query_summary": {
    "num_reviews": 100,
    "review_score": 8,
    "review_score_desc": "매우 긍정적",
    "total_positive": 984743,
    "total_negative": 136166,
    "total_reviews": 1120909
  },
  "reviews": [
    {
      "recommendationid": "132615279",
      "author": {
        "steamid": "76561198325380788",
        "num_games_owned": 63,
        "num_reviews": 10,
        "playtime_forever": 15663,
        "playtime_last_two_weeks": 111,
        "playtime_at_review": 15435,
        "last_played": 1676978764
      },
      "language": "koreana",
      "review": "근본 FPS\n라이엇이 싹 다 배껴가서 돈 버는게 굉장히 맘에 안 듦\n롤은 도타2 배끼고 롤토체스는 오토체스 배끼고 발로란트는 csgo 배끼고\n자력으로 만드는 게임이 없음 ㅋㅋ\n",
      "timestamp_created": 1676117988,
      "timestamp_updated": 1676117988,
      "voted_up": true,
      "votes_up": 37,
      "votes_funny": 0,
      "weighted_vote_score": "0.773408949375152588",
      "comment_count": 0,
      "steam_purchase": true,
      "received_for_free": false,
      "written_during_early_access": false,
      "hidden_in_steam_china": true,
      "steam_china_location": ""
    },
		...,
}
```


## 4. 기술 스택

- Frontend: `React`, `TypeScript`,
- Backend: `Spring`
- BigData: `hadoop`



## 5. 그라운드 룰

### 서로의 사생활을 존중하고 할땐 하고 쉴땐 쉬자

1. 회의 시간 지키기
2. 어려운 부분 있으면 질문 남겨놓기
3. 카톡 및 메터모스트에 확인 후 댓글 혹은 이모지 남기기



## 6. 코드 컨벤션

# **** 모두 지켜야할 규칙 ****

- 변수명 선언 시 주석 달아서 어떤 변수명인지 설명 꼭! 적기
  - 어떤 변수명인지 알 수 없습니다 !! ❌
  - 코드 다 읽으면서 분석하면 시간 낭비 !! ⏰
- 의미 없는 출력코드 ( System.out.println(), print(), console.log() 기타 등등…) 지우기!!!
  - 코드 가독성 떨어집니다 📉
  - 지저분해보여요 🥲

[세부 코드 컨벤션](https://spicy-product-5a2.notion.site/c625ca0ae7624927bdb5c52e700c74ed)

## 8. 개발문서 정리


1. [기능정의서](https://docs.google.com/spreadsheets/d/1vMAcrNfpWQSTuLhrRCsm-bIlc77D59DAHEvNLeej8dg/edit?usp=sharing)
2. [와이어프레임](https://www.figma.com/file/QmJqzdGLoYF6FVStj9EgxW/E107-%EC%99%80%EC%9D%B4%EC%96%B4-%ED%94%84%EB%A0%88%EC%9E%84?node-id=0-1&t=W38BvxXWM6Qm531O-0)
3. [ERD](https://www.erdcloud.com/d/ayPmWxr5t49gdcBEu)
4. [Sequence Diagram](https://www.notion.so/Team-E107-2109f4cdf9994489b49c7c30c15a8a23)
5. 시스템 구조도 - 개발문서 폴더