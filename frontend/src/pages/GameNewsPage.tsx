import React from "react";
import MainLayout from "../layout/MainLayout";
import GameNews from "../components/news/GameNews";

import styles from "./Page.module.css";
const GameNewsPage = () => {
  return (
    <MainLayout>
      <div className={styles.page}>
        <GameNews />
      </div>
    </MainLayout>
  );
};

export default GameNewsPage;
