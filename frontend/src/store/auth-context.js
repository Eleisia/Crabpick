import React, { useState } from "react";
import { useNavigate } from "react-router";

const AuthContext = React.createContext({
  token: "",
  userNickname: "",
  userId: 0,
  memberId: 0,
  avatarfull: "",
  loccountrycode: "",
  isLoggedIn: false,
  login: (token, userId, userNickname, avatarfull, memberId) => {},
  logout: () => {},
});

export const AuthContextProvider = (props) => {
  const initialToken = sessionStorage.getItem("token");
  const initialUserNickname = sessionStorage.getItem("userNickname");
  const initialmemberId = sessionStorage.getItem("memberId");
  const initialAvatarfull = sessionStorage.getItem("avatarfull");
  // const initialLoccountrycode = sessionStorage.getItem("loccountrycode");
  const initialUserId = sessionStorage.getItem("userId");

  const [token, setToken] = useState(initialToken);
  const [userNickname, setUserNickname] = useState(initialUserNickname);
  const [memberId, setmemberId] = useState(initialmemberId);
  const [avatarfull, setAvatarfull] = useState(initialAvatarfull);
  // const [loccountrycode, setLoccountrycode] = useState(initialLoccountrycode);
  const [userId, setUserId] = useState(initialUserId);

  // 로그인 여부 (토큰 여부)
  const userIsLoggedIn = !!token;
  const loginHandler = (userId, userNickname, avatarfull) => {
    setToken(token);
    // setmemberId(memberId);
    setUserId(userId);
    setUserNickname(userNickname);
    setAvatarfull(avatarfull);
    setmemberId(memberId);
    console.log("동작");
    // sessionStorage.setItem("token", token);
    // sessionStorage.setItem("memberId", memberId);
    sessionStorage.setItem("userId", userId);
    sessionStorage.setItem("userNickname", userNickname);
    sessionStorage.setItem("avatarfull", avatarfull);
    // sessionStorage.setITem("memberId", memberId);
  };

  const logoutHandler = () => {
    setToken(null);
    setUserId(0);
    setUserNickname("");
    setAvatarfull("");
    setmemberId(0);

    sessionStorage.removeItem("token");
    sessionStorage.removeItem("memberId");
    sessionStorage.removeItem("userId");
    sessionStorage.removeItem("userNickname");
    sessionStorage.removeItem("avatarfull");
  };

  const contextValue = {
    token: token,
    userNickname: userNickname,
    memberId: memberId,
    avatarfull: avatarfull,
    // loccountrycode: loccountrycode,
    userId: userId,
    isLoggedIn: userIsLoggedIn,

    login: loginHandler,
    logout: logoutHandler,
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {props.children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
