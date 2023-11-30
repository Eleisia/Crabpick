import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Slider, { Settings } from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import nextArrow from "../../asset/nextarrow.png";

import styles from "./RecommendCarousel.module.css";

interface Props {
  games: {
    id: number;
    appId: number;
    genre: [];
    name: string;
  }[];
}
function SampleNextArrow(props: any) {
  const { className, style, onClick } = props;
  return (
    <div
      className={className}
      style={{ ...style, display: "block" }}
      onClick={onClick}
    />
  );
}

function SamplePrevArrow(props: any) {
  const { className, style, onClick } = props;
  return (
    <div
      className={className}
      style={{ ...style, display: "block" }}
      onClick={onClick}
    />
  );
}
const BestSellerCarousel: React.FC<Props> = ({ games }) => {
  const navigate = useNavigate();
  const settings: Settings = {
    dots: true,
    infinite: true,
    speed: 500, //페이지 넘김 속도
    autoplay: true, // 자동 전환
    autoplaySpeed: 5000, // -5초
    cssEase: "linear",
    slidesToShow: 1, // 한 캐러셀에 보여질 컨텐츠 수
    slidesToScroll: 1,
    nextArrow: <SampleNextArrow />,
    prevArrow: <SamplePrevArrow />,
  };

//   const slider = (game: any) => {
//     const a = game.genre;
//     const b = a.replaceAll("'", '"');
//     const c = JSON.parse(b);
//     const genre = [];
//     for (let i = 0; i < c.length; i++) {
//       genre.push(c[i].description);
//     }
//     return genre.join(", ");
//   };

  return (
    <Slider {...settings}>
      {games.map((game: any, index) => (
        <div
          key={index}
          className={styles.carousel}
          onClick={() => navigate(`/detail/${game.gameId}`)}
        >
          <img
            src={`https://cdn.cloudflare.steamstatic.com/steam/apps/${game.gameId}/header.jpg`}
            // src={game.imageLink}
            alt={`Slide ${index}`}
            className={styles.image}
          />
          <div>
            <h1>{game.name}</h1>
            {/* {slider(game)} */}
            <div></div>
          </div>
        </div>
      ))}
    </Slider>
  );
};

export default BestSellerCarousel;
