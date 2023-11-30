import React, { useState, useContext } from "react";
import AuthContext from "../../store/auth-context";
import { useNavigate } from "react-router-dom";
import styles from "./SignInForm.module.css";
import axios from "axios";
import steamlogo from "../../asset/steamlogo.png";
import loginbuttonlogo from "../../asset/loginbuttonlogo.png";
import { log } from "console";
const SignInForm = () => {
  const [login, setLogin] = useState<Boolean>(false);
  const navigate = useNavigate();
  const authCtx = useContext(AuthContext);

  const loginHandler = () => {
    // event.preventDefault();

    // authCtx.login("sdfsdf334232", 412, 2);
    // navigate("/");
    // console.log("스팀 로그인 핸들러");
    // window.location.replace("http://localhost:4000/auth/steam");
    // window.location.replace("http://localhost:4000/auth/steam");
    window.location.replace("https://j8e107.p.ssafy.io/auth/steam");

    // // axios.get("https://j8e107.p.ssafy.io/auth/userinfo").then((response) => {
    // axios.get("http://localhost:4000/auth/userinfo").then((response) => {
    //   // console.log("메인 화면 응답" + JSON.stringify(response.data)); // 반환 데이터 전부 출력해보기
    //   // console.log("디테일 정보1 : " + response.data._json.steamid); // steamid : ex >76561199486116083
    //   // console.log("디테일 정보2 : " + response.data.displayName); // steam displayname ex> pjh6947
    //   // console.log("디테일 정보3 : " + response.data.key); // steam key ex>21680047922CC0CA013B6EFEC720919A

    //   axios
    //     .post("https://j8e107.p.ssafy.io/api/authenticate", {
    //       username: response.data.id,
    //       // password: response.data._json.primaryclanid
    //       password: "password",
    //     })
    //     .then((res) => {
    //       console.log(res.data);
    //       // authCtx.token = res.data.token;
    //       // authCtx.userId = response.data._json.steamid;
    //       authCtx.login(res.data.token, response.data.id, response.data.displayName, response.data._json.avatarfull, res.data.memberId);
    //       console.log(res.data);
    //       authCtx.token = res.data.token;
    //       authCtx.userId = response.data.id;
    //       authCtx.userNickname = response.data.displayName;
    //       authCtx.avatarfull = response.data._json.avatarfull;
    //       authCtx.memberId = res.data.memberId;
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
    //         .post("https://j8e107.p.ssafy.io/api/signup", userDto)
    //         .then((res) => {
    //           console.log("회원가입 성공", res.data);
    //           axios
    //             .post("https://j8e107.p.ssafy.io/api/authenticate", {
    //               username: response.data.id,
    //               // password: response.data._json.primaryclanid
    //               password: "password",
    //             })
    //             .then((res) => {
    //               console.log(res.data);
    //               // authCtx.token = res.data.token;
    //               // authCtx.userId = response.data._json.steamid;
    //               authCtx.login(res.data.token, response.data.id, response.data.displayName, response.data._json.avatarfull, res.data.memberId);
    //               authCtx.token = res.data.token;
    //               authCtx.userId = response.data.id;
    //               authCtx.userNickname = response.data.displayName;
    //               authCtx.avatarfull = response.data._json.avatarfull;
    //               authCtx.memberId = res.data.memberId;
    //               // window.location.reload();
    //             })
    //             .catch((err) => {});
    //         })
    //         .catch((err) => {
    //           console.log("회원가입 실패", err);
    //         });
    //       //   // 회원 가입 끝
    //     });
    // });
  };

  return (
    <div className={styles.form}>
      <div
        style={{ display: "flex", justifyContent: "center", marginTop: "10%" }}
      >
        <img src={steamlogo} style={{ width: "10%", height: "auto" }} alt="" />
      </div>
      <div style={{ display: "flex", justifyContent: "center" }}>
        <div className={styles.loginButton} onClick={loginHandler}>
          <img
            src={loginbuttonlogo}
            style={{
              width: "7%",
              height: "auto",
              marginRight: "10px",
            }}
            alt=""
          />
          {/* <span> SteamLogin</span> */}
          <h2>Steam Login</h2>
        </div>
      </div>
      <div className={styles.logindescription}>
        <span style={{ textAlign: "center" }}>
          CRABPICK에 로그인하면 추가 기능에 액세스할 수 있습니다. <br />
          <br /> 더 나은 서비스를 제공하기 위해 Steam Web API에서 귀하의 계정에
          대한 공개 정보 (steamid, 프로필 이름, 아바타 및 게임 목록
          포함)를가져옵니다. <br />
          <br />
          이 웹사이트는 Valve Corporation 또는 Steam과 관련이 없습니다. <br />
          <br />
          언제든지 계정과 모든 데이터를 삭제할 수 있습니다. 로그인한 세션을 활성
          상태로 유지하기 위해 쿠키를 사용합니다. <br />
          <br /> 로그인 버튼을 클릭하면 https://steamcommunity.com 으로 리디렉션
          되며 이미 Steam 커뮤니티에 로그인한 경우 해당 페이지에서 비밀번호를
          입력하지 않고 "로그인"을 클릭하기만 하면 됩니다. CRABPICK은 사용자
          이름이나 비밀번호를 얻지 않습니다.
        </span>
      </div>
    </div>
  );
};

export default SignInForm;
