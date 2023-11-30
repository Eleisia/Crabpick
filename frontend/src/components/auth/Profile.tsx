import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
// import AuthContext from "../../store/auth-context";
import axios from "axios";

import AuthContext from "../../store/auth-context";

import styles from "./Profile.module.css";
import steamlogo from "../../asset/steamlogo.png";

type Comment = {
  id: number;
  content: string;
  memberId: number;
  createdAt: Date;
  // updatedAt: Date;
  gameId: number; // gameId 프로퍼티 추가

  headerImg: string;
};

type Game = {
  appId: number;
  name: String;
  headerImg: string;
  playTime: number;
};
const Profile = () => {
  const navigate = useNavigate();
  const authCtx = useContext(AuthContext);

  const [comments, setComments] = useState<Comment[]>([]);
  const [games, setGames] = useState<Game[]>([]);

  const logoutHandler = () => {
    axios.get("https://j8e107.p.ssafy.io/auth/logout").catch((response) => {
      authCtx.logout();
    });
    // authCtx.logout();
    navigate("/");
  };

  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/member/comment/${authCtx.memberId}`)
      .then((res) => {
        // console.log(res.data);

        setComments(res.data.reverse().slice(0, 10));

        // setComments(res.data.reverse());
      });
  }, []);

  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/member/${authCtx.userId}/game`)
      // .get(`https://j8e107.p.ssafy.io/api/member/76561198086809301/game`)
      .then((res) => {
        if (res.data.length > 6) {
          setGames(res.data.slice(0, 6));
        } else {
          setGames(res.data);
        }
      });
  }, []);

  return (
    <div className={styles.profile}>
      <div
        style={{
          marginTop: "3%",
          marginBottom: "20px",
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <h1>MyPage</h1>
        <div
          style={{
            flexDirection: "column",
            display: "flex",
            justifyContent: "end",
            cursor: "pointer",
            margin: "0",
          }}
          onClick={logoutHandler}
        >
          <span className={styles.logout}>로그아웃</span>
        </div>
      </div>
      <hr />
      <div style={{ display: "flex", marginTop: "30px" }}>
        <img src={authCtx.avatarfull} alt="" style={{ marginRight: "30px" }} />
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
          }}
        >
          <div>
            {/* <h2>닉네임</h2> */}
            {/* <h2>{authCtx.userId}</h2> */}
            <h2>{authCtx.userNickname}</h2>
            {/* <h2>{authCtx.avatarfull}</h2> */}
            {/* <p>한 줄 소개~~~ 뭐 잡다한거~~~</p> */}
          </div>
          {/* <p>수정하러가기~</p> */}
          <div className={styles.editdiv}>
            <a
              href="https://store.steampowered.com/account/"
              target="_blank"
              rel="noreferrer"
              className={styles.edit}
            >
              <img src={steamlogo} alt="" style={{ width: "30px" }} />
              <span>Steam에서 수정하기</span>
            </a>
          </div>
        </div>
      </div>
      <div>
        <div style={{ marginTop: "5%" }}>
          <div style={{ display: "flex", alignItems: "end" }}>
            <h2 style={{ marginRight: "10px" }}>보유중인 게임 목록</h2>
            <p
              style={{ cursor: "pointer" }}
              onClick={() => navigate("/owned-games")}
            >
              보유중인 모든게임보기{">>"}
            </p>
          </div>
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
                  className={styles.owned}
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
        {/* <div style={{ marginTop: "5%" }}>
          <h2>찜 한 게임</h2>
          <p>찜 한 게임 목록들 나오기~~~</p>
        </div> */}
        <div style={{ marginTop: "5%", marginBottom: "5%" }}>
          <h2>나의 최근 {comments.length}개 코멘트 모아보기</h2>
          {comments.map((comment) => (
            // <div>{comment.memberId}</div>
            <div>
              <div style={{ width: "100%", display: "flex" }}>
                <img
                  src={comment.headerImg}
                  alt=""
                  style={{ width: "10%", marginRight: "30px" }}
                />
                <div
                  style={{
                    flexDirection: "column",
                    display: "flex",
                    justifyContent: "space-between",
                  }}
                >
                  <div>{comment.content}</div>
                  <div
                    style={{ cursor: "pointer" }}
                    onClick={() => navigate(`/detail/${comment.gameId}`)}
                  >
                    자세히 보러가기
                  </div>
                </div>
              </div>
              <hr />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Profile;
