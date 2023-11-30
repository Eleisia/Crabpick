import React, { useEffect, useRef, useState, useContext } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import AuthContext from "../../store/auth-context";
import DropdownTrigger from "./DropDown";
import logo from "../../asset/logo.png";
import styles from "./NavBar.module.css";
import axios from "axios";
const MenuBar = () => {
  const navigate = useNavigate();

  const [dropSearch, setDropSearch] = useState<Boolean>(false);
  const [searchInput, setSearchInput] = useState<string>("");
  const [gameList, setGameList] = useState<any>([]);
  const [filteredGameList, setFilteredGameList] = useState<any>(null);
  const [filteredGamePage, setFilteredGamePage] = useState<number>(1);

  const authCtx = useContext(AuthContext);
  const isLoggedIn = authCtx.isLoggedIn;

  const logoutHandler = () => {
    authCtx.logout();
    navigate("/");
  };
  // 전체 게임 목록 가져오기
  useEffect(() => {
    async function getGameList() {
      await axios
        .get("https://j8e107.p.ssafy.io/api/game")
        .then((res) => {
          setGameList(res.data);
        })
        .catch((e) => {
          console.log(e);
        });
    }
    getGameList();
  }, []);

  // home으로 이동
  const home = () => {
    navigate("/");
    window.scrollTo(0, 0);
  };

  const goMypage = () => {
    navigate(`/mypage`);
    window.scrollTo(0, 0);
  };

  // 검색창에 쓴 값 저장하기
  const searchInputHandler = (e: any) => {
    setSearchInput(e.target.value);
  };
  // 검색하기
  const searchHandler = (e: any) => {
    setDropSearch(false);
    navigate("/search", {
      state: { searchInput: searchInput, page: filteredGamePage, filteredGameList: filteredGameList },
    });
  };
  // Enter로 검색하기
  const pressEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      setDropSearch(false);
      navigate("/search", {
        state: { searchInput: searchInput, page: filteredGamePage, filteredGameList: filteredGameList },
      })
    }
  };

  useEffect(() => {
    setDropSearch(false);
    setSearchInput("");
    window.scrollTo(0, 0);
  }, [window.location.pathname]);

  // 검색창을 이용중인지 확인하여 dropdown 하기
  const getFocus = () => {
    setDropSearch(true);
  };

  const dropdownRef = useRef<HTMLDivElement>(null);

  // 클릭하면 실행
  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  // 검색창 이외의 곳을 클릭 하였는지 확인
  const handleClickOutside = (event: any) => {
    if (dropdownRef.current && dropdownRef.current.contains(event.target)) {
      setDropSearch(true);
    } else {
      setDropSearch(false);
    }
  };

  // 검색창에 쓴 값으로 게임리스트 필터링 하기
  useEffect(() => {
    axios
      .get(`https://j8e107.p.ssafy.io/api/game/name/${searchInput}`)
      .then((res) => {
        setFilteredGameList(res.data.data);
        setFilteredGamePage(res.data.pages)
      })
      .catch((e) => {
      });
  }, [searchInput]);

  return (
    <>
      <div className={styles.navBar}>
        <div className={styles.navLogo}>
          <img src={logo} alt="" onClick={home} className={styles.logo} />
        </div>
        <div className={styles.links}>
          <Link to="/game-news">게임 뉴스</Link>
        </div>
        {/* 검색 */}
        <div className={styles.search} ref={dropdownRef}>
          <input
            type="text"
            onChange={searchInputHandler}
            onKeyDown={pressEnter}
            onFocus={getFocus}
            value={searchInput}
          />
          <input type="button" value="검색" onClick={searchHandler}></input>
          <div className={styles.dropDown}>
            {searchInput !== "" && dropSearch ? (
              <DropdownTrigger filteredGameList={filteredGameList} />
            ) : null}
          </div>
        </div>
        {/* <Link to="/mypage/:nickName">마이페이지</Link> */}

        {/* 로그인X -> 로그인 링크 /  로그인 O -> 프로필 사진 */}
        {/* {login? <img>프로필 사진</img> : <Link to="/signin">로그인</Link> } */}
        {!isLoggedIn ? (
          <div className={styles.links}>
            <Link to="/signin">로그인</Link>
          </div>
        ) : (
          <div style={{ display: "flex" }} className={styles.navProfile}>
            <img
              src={authCtx.avatarfull}
              alt=""
              style={{ width: "30%" }}
              onClick={goMypage}
            />
          </div>
        )}
      </div>
    </>
  );
};

export default MenuBar;
