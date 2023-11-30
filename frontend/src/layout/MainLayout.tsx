import React from "react";
import NavBar from "../components/common/NavBar";
import Footer from "../components/common/Footer";

import { useContext } from "react";
import AuthContext from "../store/auth-context";
//내가 추가한 부분
import axios from "axios";
import { useEffect } from "react";
//
import styles from "./Layout.module.css";
import { log } from "console";
interface Props {
  children: JSX.Element | JSX.Element[];
}
const MainLayout = ({ children }: Props) => {
  const authCtx = useContext(AuthContext);
  // useEffect(() => {
  //로그인 했거나 or 로그인 안하고 단순 메인 화면 접속시 -> node.js 에 userinfo 데이터 요청 -> 데이터 있으면 바로 spring에 요청 보내기
  // axios.get("http://j8e107.p.ssafy.io:4000/auth/userinfo").then((response) => {
  //   // console.log("메인 화면 응답" + JSON.stringify(response.data)); // 반환 데이터 전부 출력해보기
  //   // console.log("디테일 정보1 : " + response.data._json.steamid); // steamid : ex >76561199486116083
  //   // console.log("디테일 정보2 : " + response.data.displayName); // steam displayname ex> pjh6947
  //   // console.log("디테일 정보3 : " + response.data.key); // steam key ex>21680047922CC0CA013B6EFEC720919A

  //   axios
  //     .post("http://j8e107.p.ssafy.io:8080/api/authenticate", {
  //       username: response.data.id,
  //       // password: response.data._json.primaryclanid
  //       password: "password",
  //     })
  //     .then((res) => {
  //       console.log(res.data);
  //       // authCtx.token = res.data.token;
  //       // authCtx.userId = response.data._json.steamid;
  //       authCtx.login(
  //         res.data.token,
  //         response.data.id,
  //         response.data.displayName,
  //         response.data._json.avatarfull,
  //         res.data.memberId
  //       );
  //       authCtx.userSequence = res.data.user_id;
  //       authCtx.userId = response.data.id;
  //       authCtx.userNickname = response.data.displayName;
  //       authCtx.avatarfull = response.data._json.avatarfull;
  //       authCtx.userSequence = res.data.memberId;
  //     })
  //     .catch((err) => {
  //       // 처음 방문한 손님이니까 우리 유저 db에 없음
  //       // 모달 띄워서 확인 누르면 회원가입 진행하도록
  //       // 모달 컴포넌트 만들기
  //       console.log(err);
  //       // 만약 확인을 누르면 steaid와 primaryclanid(password)를 바디에 담아 axios 회원가입 요청
  //       // 회원가입 성공하면
  //       // 로그인 axios를 보내 저기 위에 있는 api/authenticate 그대로
  //       // 실패하면
  //       // console.log(err) 출력

  //       // 20220403 집에서 하기

  //       // 회원 가입 시작
  //       //- 회원 가입 준비물 userDto
  //       let userDto = {
  //         username: response.data.id,
  //         password: "password",
  //         nickname: response.data.displayName,
  //       };
  //       // 회원 가입 요청
  //       axios
  //         .post("http://j8e107.p.ssafy.io:8080/api/signup", userDto)
  //         .then((res) => {
  //           console.log("회원가입 성공", res.data);
  //           axios
  //             .post("http://j8e107.p.ssafy.io:8080/api/authenticate", {
  //               username: response.data.id,
  //               // password: response.data._json.primaryclanid
  //               password: "password",
  //             })
  //             .then((res) => {
  //               // console.log(res.data);
  //               // authCtx.token = res.data.token;
  //               // authCtx.userId = response.data._json.steamid;
  //               authCtx.login(
  //                 res.data.token,
  //                 response.data.id,
  //                 response.data.displayName,
  //                 response.data._json.avatarfull,
  //                 res.data.memberId
  //               );
  //               authCtx.userSequence = res.data.user_id;
  //               authCtx.userId = response.data.id;
  //               authCtx.userNickname = response.data.displayName;
  //               authCtx.avatarfull = response.data._json.avatarfull;
  //               authCtx.userSequence = res.data.memberId;
  //             window.location.reload()
  //             })
  //             .catch((err) => {});
  //         })
  //         .catch((err) => {
  //           console.log("회원가입 실패", err);
  //         });
  //       //   // 회원 가입 끝
  //     });
  // });
  // }, []);
  return (
    <div className={styles.bgcolor}>
      <NavBar />
      {children}
      <Footer />
    </div>
  );
};

export default MainLayout;
