/**
 * Basic example demonstrating passport-steam usage within Express framework
 * 방법 조사
 * 1. 리액트 소켓 <-> 서버 소켓 통신
 *
 */
var express = require("express"),
  passport = require("passport"),
  util = require("util"),
  session = require("express-session"),
  SteamStrategy = require("../../").Strategy;
const fs = require("fs");
const store = require("store");

var userInfoAllTime;
// Passport session setup.
//   To support persistent login sessions, Passport needs to be able to
//   serialize users into and deserialize users out of the session.  Typically,
//   this will be as simple as storing the user ID when serializing, and finding
//   the user by ID when deserializing.  However, since this example does not
//   have a database of user records, the complete Steam profile is serialized
//   and deserialized.
/*여권 세션 설정.
영구 로그인 세션을 지원하기 위해 Passport는 사용자를 세션에 직렬화하고 사용자를 세션에서 직렬화할 수 있어야 합니다.
일반적으로 이것은 직렬화할 때 사용자 ID를 저장하고 역직렬화할 때 ID로 사용자를 찾는 것만큼 간단합니다.  
그러나 이 예제에는 사용자 레코드의 데이터베이스가 없으므로 전체 스팀 프로파일은 직렬화되고 역직렬화됩니다. */
passport.serializeUser(function (user, done) {
  console.log("리리2##app.js -> passport.serializeUser"); //@@리턴2.
  done(null, user);
});

passport.deserializeUser(function (obj, done) {
  console.log("리리4##app.js -> passport.deserializeUser"); //@@리턴4.
  done(null, obj);
});

// Use the SteamStrategy within Passport.
//   Strategies in passport require a `validate` function, which accept
//   credentials (in this case, an OpenID identifier and profile), and invoke a
//   callback with a user object.
/* 여권 내 스팀 전략을 사용하십시오.
여권 전략에는 자격 증명(이 경우 오픈)을 허용하는 'validate' 기능이 필요하다ID 식별자 및 프로필)을 사용하여 
사용자 개체와의 콜백을 호출합니다.*/
passport.use(
  new SteamStrategy(
    {
      // returnURL: "http://j8e107.p.ssafy.io:4000/auth/steam/return",
      returnURL: "https://j8e107.p.ssafy.io/auth/steam/return",
      // returnURL: "http://localhost:4000/auth/steam/return",
      // realm: "http://j8e107.p.ssafy.io:4000/",
      realm: "https://j8e107.p.ssafy.io/",
      // realm: "http://localhost:4000/",
      apiKey: "21680047922CC0CA013B6EFEC720919A",
    },
    function (identifier, profile, done) {
      console.log("리리1##app.js -> passport.use - new SteamStrategy"); //@@ 리턴1.
      console.log(identifier);
      console.log("----------passport.use(new SteamStrategy)------------");
      console.log(profile);

      // 쿠키를 브라우저에 저장
      // res.cookie("profile", JSON.stringify(profile), { path: "https://j8e107.p.ssafy.io/" });

      // userInfoAllTime = profile;
      console.log("----------------------------------------------------");
      console.log(done);
      // asynchronous verification, for effect...
      process.nextTick(function () {
        // To keep the example simple, the user's Steam profile is returned to
        // represent the logged-in user.  In a typical application, you would want
        // to associate the Steam account with a user record in your database,
        // and return that user instead.
        /*예제를 단순하게 유지하기 위해 사용자의 Steam 프로필은 로그인한 사용자를 나타내기 위해 반환됩니다.  
        일반적인 응용 프로그램에서는 데이터베이스의 사용자 레코드에 스팀 계정을 연결하고 대신 해당 사용자를 반환하려고 합니다.
        */
        profile.identifier = identifier;
        return done(null, profile);
      });
    }
  )
);

var app = express();
const cors = require("cors");

app.use(cors("*"));

// configure Express
app.set("views", __dirname + "/views"); //템플릿 파일이 저장된 디렉토리 경로를 설정합니다
app.set("view engine", "ejs"); //사용할 템플릿 엔진의 이름을 설정합니다.

app.use(
  session({
    secret: "your secret",
    name: "name of session id",
    resave: true,
    saveUninitialized: true,
  })
);

// Initialize Passport!  Also use passport.session() middleware, to support
// persistent login sessions (recommended).
app.use(passport.initialize());
app.use(passport.session());
app.use(express.static(__dirname + "/../../public"));

// app.get("/auth", function (req, res) {
//   console.log("리리5##app.js -> /"); //@@리턴5.
//   // res.redirect("/auth/steam");
//   res.render("index", { user: req.user });
// });

// app.get("/account", ensureAuthenticated, function (req, res) {
//   console.log("##app.js -> /account");
//   res.render("account", { user: req.user });
// });

// app.get("/auth/user/:id", (req, res) => {
//   //@@ 존재 확인하고 있으면 data반환 후 삭제 -> 없으면 로그인창 리다이렉트
//   console.log("/auth/:id경로 - store 확인");
//   console.log(req.params.id);
//   console.log(store.get(req.params.id));
//   store.remove(req.params.id);
// });

// app.get("/auth/logout", function (req, res) {
//   console.log("##app.js -> /logout");
//   req.logout();
//   userInfoAllTime = null;

//   res.redirect("https://j8e107.p.ssafy.io?steamid=116184846148616");

//   // res.redirect("http://localhost:3000/");
// });

// GET /auth/steam
//   Use passport.authenticate() as route middleware to authenticate the
//   request.  The first step in Steam authentication will involve redirecting
//   the user to steamcommunity.com.  After authenticating, Steam will redirect the
//   user back to this application at /auth/steam/return
/*'passport.authenticate ()'을 사용합니다. 요청을 인증하는 경로 미들웨어로 지정합니다.  
   Steam 인증의 첫 번째 단계는 리디렉션을 포함합니다 
   사용자가 steamcommunity.com 에 접속할 수 있습니다.
인증 후, 스팀은 사용자를 /auth/steam/return에서 이 애플리케이션으로 다시 리디렉션합니다 */

//@@1.리액트에서 신호가 와서 입장 //@@2.steam로그인 바로 리다이렉트

app.get(
  "/auth/steam",
  passport.authenticate("steam", { failureRedirect: "/" }),
  function (req, res) {
    // //@@3. steam로그인 페이지로 으로 이동
    // console.log("##app.js -> /auth/steam");
    // console.log("app.js -> /auth/steam");
    // console.log("----------app.get('/auth/steam')------------");
    // console.log(res.data);
    // // res.redirect("https://j8e107.p.ssafy.io/"); //리액트로 리다이렉트
    // res.redirect("http://localhost:3000/"); //리액트로 리다이렉트
  }
);

app.get("/auth/userinfo/:id", (req, res) => {
  console.log("##app.js -> /auth/userinfo/:id[store확인]");
  //@@ 존재 확인하고 있으면 data반환 후 삭제 -> 없으면 로그인창 리다이렉트
  // console.log(req.params.id);
  // console.log(store.get(req.params.id));
  let data = store.get(req.params.id);

  if (data) {
    console.log("데이터가 이써요~~~!!!@!@!@");
    console.log("data 출력 : ");
    console.log(data);
    store.remove(req.params.id);
    res.send(data);
  } else {
    console.log("데이터가 없어요~~~");
    // res.redirect("http://localhost:3000/");
    res.redirect("https://j8e107.p.ssafy.io");
  }
});

// GET /auth/steam/return
//   Use passport.authenticate() as route middleware to authenticate the
//   request.  If authentication fails, the user will be redirected back to the
//   login page.  Otherwise, the primary route function function will be called,
//   which, in this example, will redirect the user to the home page.
/*'passport.authenticate ()'을 사용합니다. 요청을 인증하는 경로 미들웨어로 지정합니다.  
인증에 실패하면 사용자는 다시 로그인 페이지로 리디렉션됩니다.  
그렇지 않으면 기본 경로 함수가 호출되며, 이 예에서는 홈 페이지로 사용자를 리디렉션합니다. */
app.get(
  "/auth/steam/return",
  // passport.authenticate("steam", { failureRedirect: "http://localhost:3000/" }),
  passport.authenticate("steam", {
    failureRedirect: "https://j8e107.p.ssafy.io/",
  }),
  function (req, res) {
    //@@ 로그인 실패 시 리액트의 로그인 창으로 리다이렉트 ㄱㄱ
    console.log("리리3##app.js -> /auth/steam/return"); //@@ 리턴3.
    console.log("----------app.get('/auth/steam/return')------------");
    console.log(req._passport);
    console.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    console.log(req._passport.session.user);
    //res.send(req._passport.session.user);
    store.set(req._passport.session.user._json.steamid, {
      data: req._passport.session.user,
    }); //@@store에 저장
    // res.redirect("https://j8e107.p.ssafy.io/"); //react로 리다이렉트
    // res.redirect(
    //   "http://localhost:3000/steamid?steamid=" +
    //     req._passport.session.user._json.steamid
    // ); //react로 리다이렉트  -> 쿼리스트리으로 steamid 보내야함 -> 리액트에서 쿼리스트링으로 steamid를 받아야함 -> 리액트에서 노드 호출(steamid 포함해서) -> 노드에서 store에서 steamid로 찾아서 리액트로 리턴 -> 현재 유저 데이터 삭제 (로그아웃 노드로 보낼 필요 없음)
    res.redirect(
      "https://j8e107.p.ssafy.io/steamid?steamid=" +
        req._passport.session.user._json.steamid
    ); //react로 리다이렉트  -> 쿼리스트리으로 steamid 보내야함 -> 리액트에서 쿼리스트링으로 steamid를 받아야함 -> 리액트에서 노드 호출(steamid 포함해서) -> 노드에서 store에서 steamid로 찾아서 리액트로 리턴 -> 현재 유저 데이터 삭제 (로그아웃 노드로 보낼 필요 없음)
  }
);

// console.log("파일 경로 : " + __filename);
// console.log("파일 경로 : " + __dirname);
app.listen(4000);

// Simple route middleware to ensure user is authenticated.
//   Use this route middleware on any resource that needs to be protected.  If
//   the request is authenticated (typically via a persistent login session),
//   the request will proceed.  Otherwise, the user will be redirected to the
//   login page.
/*사용자 인증을 보장하는 간단한 경로 미들웨어. 
보호해야 하는 모든 리소스에서 이 경로 미들웨어를 사용하십시오.  
요청이 인증된 경우(일반적으로 영구 로그인 세션을 통해) 요청이 진행됩니다.  
그렇지 않으면 사용자가 로그인 페이지로 리디렉션됩니다. */
function ensureAuthenticated(req, res, next) {
  console.log("##app.js -> ensureAuthenticated");
  if (req.isAuthenticated()) {
    return next();
  }
  res.redirect("/");
}
