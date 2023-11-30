import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Slider, { Settings } from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import classes from "./RecommendCarousel.module.css";
import styles from "./SaleCarousel.module.css";

interface Props {
  salegames: {
    name: string;
    gameId: number;
    imageLink: string;
    originalPrice: number;
    finalPrice: number;
    discountPercent: number;
  }[];
}

const Carousel: React.FC<Props> = ({ salegames }) => {
  const navigate = useNavigate();
  const settings = {
    className: "center",
    centerMode: true,
    infinite: true,
    centerPadding: "10px",
    slidesToShow: 2,
    speed: 500,
  };

  const handleClick = (id: number) => {
    navigate(`/detail/${id}`);
    window.scrollTo(0, 0);
  };
  return (
    <Slider {...settings}>
      {salegames.map((game, index) => (
        <div className={classes.centerimg} key={index}>
          <div onClick={() => handleClick(game.gameId)}>
            <img src={game.imageLink} alt="" />
            <div className={styles.discount_block}>
              <div className={styles.discount_pct}>
                -{game.discountPercent}%
              </div>
              <div className={styles.discount_prices}>
                <div
                  className={`${styles.discount_original_price} ${styles.cancelled_price}`}
                >
                  ￦ {game.originalPrice.toLocaleString()}
                </div>
                <div className={styles.discount_final_price}>
                  ￦ {game.finalPrice.toLocaleString()}
                </div>
              </div>
            </div>
          </div>
        </div>
      ))}
    </Slider>
  );
};

export default Carousel;
