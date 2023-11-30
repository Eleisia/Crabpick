import React, { useContext, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import AuthContext from "../store/auth-context";

const SteamIdPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const searchParams = new URLSearchParams(location.search);
  const steamid = searchParams.get("steamid");

  const authCtx = useContext(AuthContext);

  useEffect(() => {
    axios
      // .get(`http://localhost:4000/auth/userinfo/${steamid}`)
      .get(`https://j8e107.p.ssafy.io/auth/userinfo/${steamid}`)
      .then((response) => {
        if (response.data.data) {
          console.log(response.data.data);
          console.log(response.data.data.id);
          console.log(response.data.data._json.personaname);
          console.log(response.data.data.id);
          // authCtx.userId = response.data.data.id;
          // authCtx.userNickname = response.data.data.displayName;
          // authCtx.avatarfull = response.data.data.avatarfull;
          // console.log(authCtx.userId, authCtx.userNickname, authCtx.avatarfull);
          const username = response.data.data.id;
          const nickname = response.data.data._json.personaname;
          authCtx.login(
            response.data.data.id,
            response.data.data._json.personaname,
            response.data.data._json.avatarfull
          );
          // 스팀에서 정보 받아왔으니 이제 우리 스프링에 로그인 요청
          axios
            .post("https://j8e107.p.ssafy.io/api/authenticate", {
              username: username,
              password: "password",
            })
            .then((res) => {
              console.log(res.data);
              sessionStorage.setItem("token", res.data.token);
              sessionStorage.setItem("memberId", res.data.memberId);
              window.location.reload();
            })
            // 로그인 실패다? 그럼 처음 오는 거임 일단 회원가입 강제로 시켜버리기
            .catch(() => {
              let userDto = {
                username: username,
                password: "password",
                nickname: nickname,
              };
              // 회원가입 요청
              axios
                .post("https://j8e107.p.ssafy.io/api/signup", userDto)
                .then((res) => {
                  // 회원가입 하자마자 바로 로그인 시키기
                  console.log(res.data);
                  axios
                    .post("https://j8e107.p.ssafy.io/api/authenticate", {
                      username: username,
                      password: "password",
                    })
                    .then((res) => {
                      sessionStorage.setItem("token", res.data.token);
                      sessionStorage.setItem("memberId", res.data.memberId);
                      window.location.reload();
                    });
                });
            });
        } else {
          console.log("밑에는 두번째 보내는 겁니다.");
        }

        navigate("/");
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      <span>로그인 중입니다...{steamid}</span>
    </div>
  );
};

export default SteamIdPage;
