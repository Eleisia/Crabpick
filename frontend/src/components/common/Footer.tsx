import React from "react";
import styles from "./Footer.module.css";
import { Link } from "react-router-dom";
import logo from "../../asset/logo.png";
function scrollToTop() {
  window.scrollTo({ top: 0, behavior: "smooth" });
}
const Footer = () => {
  return (
    <div className={styles.footer}>
      <div className={styles.div}>
        <div className={styles.leftDiv}>
          <div className={styles.footerLink}>
            <Link to="/">메인 페이지</Link>
            <hr />
            <Link to="/game-news" onClick={scrollToTop}>
              게임 뉴스
            </Link>
            <hr />
            <Link to="/mypage">마이페이지</Link>
          </div>
          <hr />
          <div>
            <h3 className={styles.adminHtag}>개발자들</h3>
            <div className={styles.admin}>
              <p>강동훈: Backend</p>
              <hr />
              <p>박종현: Backend</p>
              <hr />
              <p>배우찬: Frontend</p>
              <hr />
              <p>이승한: Frontend</p>
              <hr />
              <p>조용재: Backend</p>
              <hr />
              <p>최홍준: CI/CD</p>
            </div>
          </div>
        </div>
        <div className={styles.rightDiv}>
          <img src={logo} alt="" className={styles.logoImg} />
        </div>
      </div>
      <div className={styles.copyright}>
        Copyright 2023. CrabPICK All right reserved.
      </div>
    </div>
  );
};

export default Footer;
