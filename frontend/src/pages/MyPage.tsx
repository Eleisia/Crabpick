import React from "react";
import MyPageLayout from "../layout/MyPageLayout";
import MainLayout from "../layout/MainLayout";
import Profile from "../components/auth/Profile";
import styles from "./Page.module.css";

const MyPage = () => {
  return (
    <MainLayout>
      <div className={styles.page}>
        <Profile />
      </div>
    </MainLayout>
  );
};

export default MyPage;
