from math import log2

# playtimeFactor : 분 시간 플레이타임 받고 유저의 플레이타임 별 가중치
def calc_playtime_factor(playtime):
    if playtime < 200:
        return 1
    return int(log2(playtime/6000)) + 1 # 800시간 이샹 x4, 400시간 이상 x3, 200시간 이상 x2
# {(유져 보유 게임 id) : {playtime: (플레이시간), data: {게임 별 추천 목록 딕셔너리}}, (유져 보유 게임 id) : {playtime: (플레이시간), data: {게임 별 추천 목록 딕셔너리}}, (유져 보유 게임 id) : {playtime: (플레이시간), data: {게임 별 추천 목록 딕셔너리}}, ... 형태의 딕셔너리 생각중}
def list_of_recommand_games(user_library_recommand_dicts):
    # 전체 추천 합산 딕셔너리
    total_dict = dict()
    for user_game_id, recommand_dict in user_library_recommand_dicts.items():
        # 플레이 시간 2시간 이하 게임 제외
        if recommand_dict["playtime"] < 120:
            continue
        PLAYTIME_FACTOR = calc_playtime_factor(recommand_dict["playtime"])
        # 추천 값 연산
        for id, value in recommand_dict["data"]:
            if id in total_dict.keys():
                total_dict[id] += PLAYTIME_FACTOR * value
            else:
                total_dict[id] = PLAYTIME_FACTOR * value
    # 크기 순 정렬 후 상위 30개만 추출
    # 형태는 [(gameid, 추천점수), ...]
    result = sorted(list(total_dict.items()), reverse=True, key=lambda item: item[1])[:30]
    return result