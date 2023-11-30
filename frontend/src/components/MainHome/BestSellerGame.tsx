import React, { useState, useEffect } from "react";
import BestSellerCarousel from "./BestSellerCarousel";

const BestSellerGame = () => {
  const [bestSellerGames, setBestSellerGames] = useState([]);

  useEffect(() => {
    fetch("https://j8e107.p.ssafy.io/api/game/top")
      .then((response) => response.json())
      .then((data) => {
        setBestSellerGames(data);
      })
      .catch((error) => console.log(error));
  }, []);

  return (
    <div style={{ paddingBottom: "50px" }}>
      <h2>Best Seller GAMES</h2>
      <div style={{ width: "70%", margin: "0 auto" }}>
        <BestSellerCarousel games={bestSellerGames} />
      </div>
    </div>
  );
};

export default BestSellerGame;
