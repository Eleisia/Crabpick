import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./SearchResult.module.css";

const SearchResult = (game: any, i: number) => {
  const url = `https://cdn.cloudflare.steamstatic.com/steam/apps/${game.game.appId}/header.jpg?`

  // 클릭 시 Detail 페이지로 이동
  const navigate = useNavigate();
  const goToDetail = () => {
    navigate(`/detail/${game.game.appId}`);
  };

  return (
    <div className={styles.SearchResult} onClick={goToDetail}>
      <div className={styles.SearchGame}>
        <img src={url} alt="이미지 없음" />
        <div className={styles.gameName}>{game.game.name}</div>
      </div>
    </div>
  );
};

export default SearchResult;
