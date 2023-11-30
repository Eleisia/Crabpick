import React, { useState, useEffect } from "react";
import axios from "axios";
import TopRankGameCarousel from "./TopRankGameCarousel";

import footballmanager from "../../asset/DUMMY/footballmanager.jpg";
import battlegrounds from "../../asset/DUMMY/battlegrounds.jpg";
import hogwarts from "../../asset/DUMMY/hogwarts.jpg";

const TopRankGame = () => {
  const [games, setGames] = useState([]);

  useEffect(() => {
    fetch("https://j8e107.p.ssafy.io/api/game")
      .then((response) => response.json())
      .then((data) => {
        setGames(data);
      })
      .catch((error) => console.log(error));
  }, []);

  return (
    <div style={{ paddingBottom: "50px" }}>
      <h2>Recommand GAMES</h2>
      <div style={{ width: "70%", margin: "0 auto" }}>
        <TopRankGameCarousel games={games} />
      </div>
    </div>
  );
};

export default TopRankGame;
