import React, { useState, useEffect, useContext } from "react";
import RecommendCarousel from "./RecommendCarousel";
import AuthContext from "../../store/auth-context";
import axios from "axios";

const RecommandGame = () => {
  const [recommendGames, setRecommendGames] = useState([]);
  // const [userGames, setUserGames] = useState<any>([]);

  const authCtx = useContext(AuthContext);

  useEffect(() => {
    axios
      .get(
        `https://j8e107.p.ssafy.io/api/game/recommend/user/${authCtx.userId}`
      )
      // .get(`https://j8e107.p.ssafy.io/api/game/recommend/user/76561198358343891`)
      .then((res) => {
        setRecommendGames(res.data.data);
      });
  }, []);

  // useEffect(() => {
  //   axios
  //     .get(`https://j8e107.p.ssafy.io/api/member/${authCtx.userId}/game`)
  //     .then((res: any) => {
  //       console.log('성공', res.data);
  //       res.data.map((game: any, idx: number) => {
  //         setUserGames((userGames: any) => [
  //           ...userGames,
  //           { id: game.appId, playTime: game.playTime },
  //         ]);
  //       });
  //       // .catch((err: any) => {
  //       // console.log("err", err);
  //     });
  // }, []);
  // console.log(userGames);

  // useEffect(() => {
  //   axios({
  //     method: "post", // [요청 타입]
  //     url: `https://j8e107.p.ssafy.io/api/game/recommend/user/76561198358343891`,
  //     // `https://j8e107.p.ssafy.io/api/game/recommend/user/${authCtx.userId}`,
  //     data: { Body: JSON.stringify(userGames) }, // [요청 데이터]
  //     headers: {
  //       "Content-Type": "application/json; charset=utf-8",
  //     }, // [요청 헤더]
  //     //responseType: "json" // [응답 데이터 : stream , json]
  //   }).then((res) => {
  //     console.log("추천 요청 성공", res.data);
  //     setRecommandGames(res.data);
  //   });
  // }, [userGames]);
  // console.log("recommandGames", recommandGames);

  return (
    <div style={{ paddingBottom: "50px" }}>
      {recommendGames === undefined || recommendGames.length === 0 ? null : (
        <div>
          <h2>CRABPICK GAMES</h2>
          <div style={{ width: "70%", margin: "0 auto" }}>
            <RecommendCarousel games={recommendGames} />
          </div>
        </div>
      )}
    </div>
  );
};

export default RecommandGame;
