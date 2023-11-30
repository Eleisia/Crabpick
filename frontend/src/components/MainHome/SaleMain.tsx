import React, { useState, useEffect } from "react";
import SaleCarousel from "./SaleCarousel";

const SaleGame = () => {
  const [saleGames, setSaleGames] = useState([]);

  useEffect(() => {
    fetch("https://j8e107.p.ssafy.io/api/game/discount")
      .then((response) => response.json())
      .then((data) => {
        setSaleGames(data);
      })
      .catch((error) => console.log(error));
  }, []);

  return (
    <div style={{ paddingBottom: "50px" }}>
      <h2>Sale GAMES</h2>
      <div style={{ width: "90%", margin: "0 auto" }}>
        <SaleCarousel salegames={saleGames} />
      </div>
    </div>
  );
};

export default SaleGame;
