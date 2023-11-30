import React, { useContext } from "react";
import MainLayout from "../layout/MainLayout";
import TopRankGame from "../components/MainHome/TopRankGame";
import RecommendGame from "../components/MainHome/RecommendGame";
import SaleMain from "../components/MainHome/SaleMain";
import styles from "./Page.module.css";
import BestSellerGame from "../components/MainHome/BestSellerGame";
import AuthContext from "../store/auth-context";
const Main = () => {
  const authCtx = useContext(AuthContext);
  return (
    <MainLayout>
      <div className={styles.page}>
        {authCtx.isLoggedIn ? <RecommendGame /> : null}
        {/* <RecommendGame /> */}
        {/* <TopRankGame /> */}
        <BestSellerGame />
        <SaleMain />
      </div>
    </MainLayout>
  );
};

export default Main;
