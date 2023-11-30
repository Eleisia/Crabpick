import React, { useState, useEffect } from "react";
import { useParams } from "react-router";
import { useNavigate } from "react-router-dom";
import Slider, { Settings } from "react-slick";
import styles from "./Detail.module.css";
import deleteImg from "../../asset/deleteImg.png";
import steamlogo from "../../asset/steamlogo.png";
import favorite from "../../asset/favorite.png";
import no_favorite from "../../asset/no_favorite.png";
import axios from "axios";

import { useContext } from "react";
import AuthContext from "../../store/auth-context";

// import Comment from "./Comment";

interface gameData {
  ageLimit: number;
  appId: number;
  avgPlaytime: object;
  comments: [];
  developer: string;
  genre: string;
  id: number;
  imgUrl: string;
  mood: string;
  name: string;
  release: string;
  steamLink: string;
  trailer_url: string;
  wordCloud: string;
}

interface months {
  [key: string]: string;
}

const MAX_ROWS = 5; // 최대 줄 수
const Detail = () => {
  const authCtx = useContext(AuthContext);
  const { gameId } = useParams();
  const [gameData, setGameData] = useState<gameData>();
  const [gameGenre, setGameGenre] = useState<string>("");
  const [gameDeveloper, setGameDeveloper] = useState<string>("");
  const [gameRelease, setGameRelease] = useState<string>("");
  const [gameImage, setGameImage] = useState<any>();
  const [selectedImage, setSelectedImage] = useState<any>("");
  const [selectedIdx, setSelectedIdx] = useState<number>(0);
  const [simmilarGames, setSimmilarGames] = useState<any>();
  const [simmilarGamesImage, setSimmilarGamesImage] = useState<any>([]);

  const [isFavorited, setIsFavorited] = useState(false);

  // 댓글 더보기 버튼
  const [showCount, setShowCount] = useState(10);

  // useEffect(() => {
  //   axios
  //     .get(`https://j8e107.p.ssafy.io/api/${authCtx.userId}/${gameId}`)
  //     .then((res) => {
  //       setIsFavorited(res.data.isFavorited);
  //     });
  // }, [gameId, authCtx.userId]);

  const handleFavorite = () => {
    const url = `https://j8e107.p.ssafy.io/api/member/bookmark/${authCtx.userId}/${gameId}`;
    // 즐겨 찾기가 되어 있다면
    if (isFavorited) {
      // 삭제하기
      // axios
      //   .delete(url)
      //   .then(() => setIsFavorited(false))
      //   .catch((err) => console.log(err));
      setIsFavorited(false);
    } else {
      // 즐겨찾기 안되어 있을 때
      axios
        .post(url, {
          userId: authCtx.userId,
          gameId: gameId,
        })
        .then((res) => {
          console.log("성공");
          setIsFavorited(true);
        })
        .catch((err) => {});
      setIsFavorited(true);
    }
  };

  const month: months = {
    "Jan,": "1",
    "Feb,": "2",
    "Mar,": "3",
    "Apr,": "4",
    "May,": "5",
    "Jun,": "6",
    "Jul,": "7",
    "Aug,": "8",
    "Sep,": "9",
    "Oct,": "10",
    "Nov,": "11",
    "Dec,": "12",
  };

  const navigate = useNavigate();

  /////////////////////////////데이터 받아와서 저장하기////////////////////////////////////
  // 게임 데이터
  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/game/${gameId}`)
      .then((res) => {
        setGameData(res.data);
      })
      .catch((err) => {
        alert("해당 ID를 가진 게임이 없습니다!!");
        navigate("/");
      });
  }, [gameId]);

  // 게임 트레일러
  useEffect(() => {
    setSelectedImage("");
    if (gameData) {
      let trailerA = gameData.trailer_url;
      if (trailerA != "") {
        const count = trailerA.split("id").length - 1;
        if (count < 2) {
          trailerA = trailerA.slice(1, trailerA.length - 1);
        }
        const trailerB = trailerA.replaceAll("'", '"');
        const trailerC = trailerB.replaceAll("True", "true");
        const trailerD = trailerC.replaceAll("False", "false");
        const trailerE = trailerD.replaceAll('"s', "'");
        const trailerF = trailerE.replaceAll('" ', "' ");
        const trailerG = JSON.parse(trailerF.trim());
        if (trailerG.length) {
          setGameImage(trailerG);
        } else {
          setGameImage([trailerG]);
        }
      } else {
        setGameImage([]);
      }
    }
  }, [gameData]);

  // 게임 이미지
  useEffect(() => {
    if (gameData) {
      let imageA = gameData.imgUrl;
      const count = imageA.split("id").length - 1;
      if (count < 2) {
        imageA = imageA.slice(1, imageA.length - 1);
      }
      const imageB = imageA.replaceAll("'", '"');
      const imageC = imageB.replaceAll("True", "true");
      const imageD = imageC.replaceAll("False", "false");
      const imageE = imageD.replaceAll('"s', "'");
      const imageF = imageE.replaceAll('" ', "' ");
      const imageG = JSON.parse(imageF.trim());
      setGameImage((gameImage: any) => [...gameImage, ...imageG]);
    }
  }, [gameData]);

  useEffect(() => {
    if (gameData) {
      setSelectedImage(gameImage[0]);
    }
  }, [gameImage]);

  useEffect(() => {
    if (gameImage) {
      setSelectedImage(gameImage[selectedIdx]);
    }
  }, [selectedIdx, gameImage]);

  // 게임 장르
  useEffect(() => {
    if (gameData) {
      const genreA = gameData.genre;
      const genreB = genreA.replaceAll("'", '"');
      const genreC = JSON.parse(genreB);
      const genre = [];
      for (let i = 0; i < genreC.length; i++) {
        genre.push(genreC[i].description);
      }
      setGameGenre(genre.join(", "));

      const developerA = gameData.developer;
      const developerB = developerA.replaceAll("'", '"');
      const developerC = JSON.parse(developerB);
      const developer = [];
      for (let i = 0; i < developerC.length; i++) {
        developer.push(developerC[i]);
      }
      setGameDeveloper(developer.join(", "));

      const releaseDate = gameData?.release.split(" ");
      setGameRelease(
        releaseDate[2] +
          "년 " +
          month[releaseDate[1]] +
          "월 " +
          releaseDate[0] +
          "일"
      );
    }
  }, [gameData]);

  // 비슷한 게임
  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/game/recommend/${gameId}`)
      .then((res) => {
        setSimmilarGames(res.data.slice(0, 10));
      });
  }, [gameId]);

  const goDetail = (id: number) => {
    navigate(`/detail/${id}`);
  };

  /////////////////////////////코멘트////////////////////////////////////
  const [commentRows, setCommentRows] = useState(1); // 현재 줄 수
  const handleCommentChange = (event: any) => {
    setCommentRows(event.target.value);
    // 입력된 텍스트의 줄 수 체크
    const rows = event.target.value.split("\n").length;
    if (rows <= MAX_ROWS) {
      setCommentRows(rows);
    }
  };

  const [commentList, setCommentList] = useState<
    { id: number; memberName: string; content: string; createdAt: string }[]
  >([]);

  const [nextCommentId, setNextCommentId] = useState(1);

  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/comment/${gameId}`)
      .then((response) => {
        setCommentList(response.data);
      });
  }, [nextCommentId, gameId]);

  const submitComment = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const textarea = event.currentTarget.querySelector("textarea");
    if (textarea) {
      const commentText = textarea.value.trim();
      if (commentText) {
        // setCommentList((prevList) => [
        //   ...prevList,
        //   { id: nextCommentId, nickname: "Guest", content: commentText },
        // ]);
        axios.post(
          `https://j8e107.p.ssafy.io/api/comment/${authCtx.memberId}/${gameId}`,
          {
            content: commentText,
          }
        );
        setNextCommentId(nextCommentId + 1);
        textarea.value = "";
        window.location.reload();
        document.body.scrollTop = document.body.scrollHeight;
      }
    }
  };
  const deleteComment = (id: number) => {
    axios.delete(`https://j8e107.p.ssafy.io/api/comment/${id}`).then((res) => {
      setCommentList((prevList) =>
        prevList.filter((comment) => comment.id !== id)
      );
    });
  };

  // const handleKeyPress = (event: any) => {
  //   // 현재 줄 수가 최대 줄 수와 같을 때 입력되지 않도록 처리
  //   if (event.key === "Enter") {
  //     submitComment(event)
  //   }
  // };

  const steam = () => {
    window.open(`https://store.steampowered.com/app/${gameData?.appId}/`);
  };

  return (
    <div className={styles.detail}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <h1>{gameData?.name}</h1>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          {/* <div style={{ marginRight: "1rem" }}>좋아요</div>
          <div
            style={{
              flexDirection: "column",
              display: "flex",
              justifyContent: "center",
            }}
            onClick={handleFavorite}
          >
            {isFavorited ? (
              <img
                src={favorite}
                style={{ background: "none", width: "2rem" }}
                alt=""
              />
            ) : (
              <img
                src={no_favorite}
                style={{ background: "none", width: "2rem" }}
              ></img>
            )}
          </div> */}
        </div>
      </div>
      <div id="게임소개" className={styles.gameDetail}>
        <div id="게임이미지" className={styles.gameImage}>
          {selectedImage && selectedImage.thumbnail ? (
            <video
              controls
              autoPlay
              muted
              src={selectedImage.mp4[480]}
              id="큰이미지"
              className={styles.bigImage}
            ></video>
          ) : (
            <img src={selectedImage.path_thumbnail}></img>
          )}
          <div id="스크롤" className={styles.smallImageBox}>
            <div id="작은이미지들" className={styles.smallImages}>
              {gameImage
                ? gameImage.map((image: any, idx: number) => {
                    let thumbnail = "";
                    let isVideo = false;
                    if (image.thumbnail) {
                      thumbnail = image.thumbnail;
                      isVideo = true;
                    } else {
                      thumbnail = image.path_thumbnail;
                    }
                    return (
                      <img
                        key={idx}
                        src={thumbnail}
                        onClick={() => setSelectedIdx(idx)}
                        className={styles.smallImage}
                      ></img>
                    );
                  })
                : null}
            </div>
          </div>
        </div>
        <div id="세부정보" className={styles.detailInfo}>
          <p>장르 : {gameGenre}</p>
          <p>제작사 : {gameDeveloper}</p>
          <p>
            연령제한 :{" "}
            {gameData && gameData.ageLimit === 0
              ? "없음"
              : gameData
              ? gameData.ageLimit + "세"
              : null}
          </p>
          <p>출시일 : {gameRelease}</p>
          {/* <p>평가 비율</p>
          <div id="평가비율" className={styles.ratio}>
            <meter
              min="0"
              max="100"
              low={30}
              high={70}
              value="75"
              id="평가비율바"
              className={styles.rateBar}
            ></meter>
          </div> */}
          <br />
          <div id="스팀링크">
            <img
              src={steamlogo}
              alt=""
              onClick={steam}
              className={styles.steamlogo}
            />
          </div>
        </div>
      </div>
      <h2>비슷한 게임들</h2>

      <div className={styles.simmilarGamesBox}>
        {simmilarGames && simmilarGames.length > 0 ? (
          <div id="비슷한게임들" className={styles.simmilarGames}>
            {simmilarGames
              ? simmilarGames.map((game: any, idx: number) => {
                  return (
                    <img
                      key={idx}
                      src={game.headerImg}
                      alt={`similar game ${idx}`}
                      className={styles.simmilarGame}
                      onClick={() => goDetail(game.appId)}
                      // onClick={() => goDetail(game)}
                    />
                  );
                })
              : null}
          </div>
        ) : null}
      </div>
      {/* <div id="평점">
        <div id="" className={styles.evaluate}>
          <div>평점</div>
          <div className={styles.average}>평균 별점</div>
        </div>
        <div>★★★★☆</div>
      </div> */}

      <div style={{ marginBottom: "10%" }}>
        <div style={{ position: "relative" }}>
          <h2>코멘트</h2>
          {/* <Comment /> */}
          <div>
            <div>
              {!authCtx.isLoggedIn && (
                <div
                  className={styles.notlogincomment}
                  onClick={() => navigate("/signin")}
                >
                  <span>로그인하고 댓글 작성하기</span>
                </div>
              )}
            </div>
            <form
              onSubmit={submitComment}
              style={{ filter: authCtx.isLoggedIn ? "none" : "blur(3px)" }}
            >
              <textarea
                maxLength={150}
                onChange={handleCommentChange}
                rows={commentRows}
                style={{
                  width: "100%",
                  resize: "none",
                  height: "100px",
                  fontSize: "16px",
                  // padding: "10px",
                  // border: "1px solid #ccc",
                  margin: "0",
                  padding: "0",
                  borderRadius: "4px",
                }}
                disabled={!authCtx.isLoggedIn}
              ></textarea>
              <div style={{ display: "flex", justifyContent: "flex-end" }}>
                <input
                  type="submit"
                  value="등록"
                  style={{
                    backgroundColor: "#4caf50",
                    color: "white",
                    padding: "10px",
                    fontSize: "16px",
                    border: "none",
                    borderRadius: "4px",
                    cursor: "pointer",
                  }}
                  disabled={!authCtx.isLoggedIn}
                />
              </div>
            </form>
          </div>
        </div>
        <div id="코멘트">
          {commentList.length === 0 ? (
            <div style={{ margin: "10px", textAlign: "center" }}>
              아직 댓글이 없습니다.
            </div>
          ) : (
            <div>
              <div style={{ margin: "10px" }}>댓글 {commentList.length}개</div>
              {commentList.map((comment, index) => (
                <div
                  key={index}
                  style={{
                    // border: "1px solid black",
                    borderBottom: "1px solid #ccc",
                    margin: "10px",
                    padding: "10px",
                    whiteSpace: "pre-wrap",
                    wordBreak: "break-all",
                  }}
                >
                  <div style={{ display: "flex" }}>
                    <div style={{ marginRight: "10px" }}>
                      {comment.memberName}
                    </div>
                    <div>
                      <span>
                        {new Date(comment.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>
                  <div
                    style={{ display: "flex", justifyContent: "space-between" }}
                  >
                    <div>
                      <span style={{ fontSize: "30px" }}>
                        {comment.content}
                      </span>
                    </div>

                    {/* 삭제 */}
                    {authCtx.userNickname == comment.memberName && (
                      <div style={{ alignItems: "end", display: "flex" }}>
                        <img
                          src={deleteImg}
                          style={{
                            background: "none",
                            width: "50px",
                            cursor: "pointer",
                          }}
                          alt=""
                          onClick={() => deleteComment(comment.id)}
                        />
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Detail;
