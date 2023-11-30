import React from "react";
import GamebtiLayout from "../layout/GamebtiLayout";
import Gamebti from "../components/gamebti/Gamebti";

import styles from "./Page.module.css";
const GamebtiPage = () => {
  return (
    <GamebtiLayout>
      <div className={styles.page}>
        <Gamebti />
      </div>
    </GamebtiLayout>
  );
};

export default GamebtiPage;
