import React from "react";
import MyPageLayout from "../layout/MyPageLayout";
import MainLayout from "../layout/MainLayout";
import OwnedGame from "../components/auth/OwnedGame";
import styles from "./Page.module.css";

const OwnedPage = () => {
  return (
    <MainLayout>
      <div className={styles.page}>
        <OwnedGame />
      </div>
    </MainLayout>
  );
};

export default OwnedPage;
