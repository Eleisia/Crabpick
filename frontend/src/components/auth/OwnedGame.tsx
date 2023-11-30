import React, { useEffect, useState, useContext } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AuthContext from "../../store/auth-context";

import styles from "./Profile.module.css";
import axios from "axios";

type Game = {
  appId: number;
  name: String;
  headerImg: string;
  playTime: number;
};

const OwnedGame = () => {
  const [games, setGames] = useState<Game[]>([]);
  const navigate = useNavigate();
  const authCtx = useContext(AuthContext);
  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/member/${authCtx.userId}/game`)
      .then((res) => {
        setGames(res.data);
      });
  }, []);

  return (
    <div>
      {games.length === 0 ? (
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div>
            <p>
              프로필을 비공개로 했거나 보유중인 게임이 없습니다. 게임 세부
              정보를 공개해주세요{" "}
            </p>
            <div
              onClick={() =>
                (window.location.href = `https://steamcommunity.com/profiles/${authCtx.userId}/edit/settings`)
              }
              style={{
                display: "flex",
                justifyContent: "center",
                cursor: "pointer",
              }}
            >
              수정하러 가기
            </div>
          </div>
        </div>
      ) : (
        <div style={{ display: "flex", flexWrap: "wrap" }}>
          {games.map((game, index) => (
            <div
              style={{ flex: "1 0 20%", padding: "10px" }}
              key={index}
              onClick={() => navigate(`/detail/${game.appId}`)}
            >
              <img src={game.headerImg} alt="" />
              <p className={styles.ownedGame}>{game.name}</p>
              <p>플레이타임: 약 {game.playTime}시간</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default OwnedGame;
