const { default: axios } = require("axios");
const FormData = require("form-data");
/**
 * Module dependencies.
 */
var util = require("util"),
  OpenIDStrategy = require("@passport-next/passport-openid").Strategy,
  SteamWebAPI = require("steam-web"),
  util = require("util");
console.log("##strategy.js -> head===================");
/**
 * Retrieve user's Steam profile information.
 *  사용자의 스팀 프로필 정보를 검색합니다.
 * @param  {String} key     Steam WebAPI key.
 * @param  {String} steamID SteamID64.
 * @param callback
 * @return {Object}         User's Steam profile.
 */
async function getUserProfile(key, steamID, callback) {
  console.log("##strategy.js -> function getUserProfile");
  var steam = new SteamWebAPI({ apiKey: key, format: "json" });
  let userInfo = {};

  let ownedGamesPromise = new Promise((resolve, reject) => {
    steam.getOwnedGames({
      //steamids: [steamID],
      steamid: steamID.split("/").pop(), //@@ steamID가 url형식이라서 맨뒤 고유 숫자만 자름
      include_appinfo: true,
      include_played_free_games: true,
      appids_filter: [],
      callback: function (err, result) {
        if (err) {
          // console.log("##strategy.js(getUserProfile -> steam.getOwnedGames) ->", err);
          reject(err);
        } else {
          userInfo.ownedGames = result;
          // console.log("##strategy.js2 (getUserProfile -> steam.getOwnedGames) ->" + util.inspect(result, { depth: null }), "key", key, "steamID", steamID, "steam", steam);
          resolve(result);
        }
      },
    });
  });

  let playerSummariesPromise = new Promise((resolve, reject) => {
    steam.getPlayerSummaries({
      steamids: [steamID],
      callback: function (err, result) {
        // console.log("##strategy.js ->" + util.inspect(result, { depth: null }), "key", key, "steamID", steamID, "steam", steam);
        if (err) {
          return callback(err);
        }

        if (!(result && result.response && Array.isArray(result.response.players) && result.response.players.length > 0)) {
          return callback(new Error("Malformed response while retrieving user's Steam profile information"));
        }

        var profile = {
          provider: "steam",
          _json: result.response.players[0],
          id: result.response.players[0].steamid,
          key: key,
          displayName: result.response.players[0].personaname,
          photos: [
            {
              value: result.response.players[0].avatar,
            },
            {
              value: result.response.players[0].avatarmedium,
            },
            {
              value: result.response.players[0].avatarfull,
            },
          ],
        };

        userInfo.userProfile = profile;
        //console.log("=======================================userInfo test === ", util.inspect(userInfo, { depth: null }) + "\n============================================");

        callback(null, profile);
        resolve(profile);
      },
    });
  });
  // console.log("=======================================userInfo test === ", util.inspect(userInfo, { depth: null }) + "\n============================================");
}

async function getUserProfileAndSendSpring(key, steamID) {
  console.log("##strategy.js -> function getUserProfileAndSendSpring");
  var steam = new SteamWebAPI({ apiKey: key, format: "json" });
  let userInfo = {};

  let ownedGamesPromise = await new Promise((resolve, reject) => {
    steam.getOwnedGames({
      //steamids: [steamID],
      steamid: steamID.split("/").pop(), //@@ steamID가 url형식이라서 맨뒤 고유 숫자만 자름
      include_appinfo: true,
      include_played_free_games: true,
      appids_filter: [],
      callback: function (err, result) {
        if (err) {
          // console.log("##strategy.js(getUserProfile -> steam.getOwnedGames) ->", err);
          reject(err);
        } else {
          userInfo.ownedGames = result.response;
          // console.log("##strategy.js2 (getUserProfile -> steam.getOwnedGames) ->" + util.inspect(result, { depth: null }), "key", key, "steamID", steamID, "steam", steam);
          resolve(result);
        }
      },
    });
  });

  let playerSummariesPromise = await new Promise((resolve, reject) => {
    steam.getPlayerSummaries({
      steamids: [steamID],
      callback: function (err, result) {
        // console.log("##strategy.js ->" + util.inspect(result, { depth: null }), "key", key, "steamID", steamID, "steam", steam);

        if (err || !(result && result.response && Array.isArray(result.response.players) && result.response.players.length > 0)) {
          console.log("##strategy.js -> getUserProfile -> err", err);
        }

        var profile = result.response.players[0];
        profile.key = key;
        userInfo.userProfile = profile;

        resolve(profile);
      },
    });
  });
  // console.log("=======================================userInfo test === ", util.inspect(userInfo, { depth: null }) + "\n============================================");
  //To spring boot ->
  // axios
  //   .post(
  //     "http://localhost:8080/test",
  //     userInfo /*{
  //     ownedGames: ownedGamesPromise,
  //     userProfile: playerSummariesPromise,
  //   }*/
  //   ) //here
  //   .then((res) => {
  //     // console.log("#############strategy.js -> axios.get ->", res.data);
  //     // console.log("******************************inner act doing******************************************");
  //     // var formData = new FormData();
  //     // formData.append("username", "76561199486116083");
  //     // formData.append("password", "1234");
  //     // axios
  //     //   .post("http://localhost:8080/loginProc", formData /*{ body: formData } { formData } { steamid: "abcde" } { steamid: "abcde" } formData*/, {
  //     //     // headers: {
  //     //     //   "Content-Type": `application/x-www-form-urlencoded`, // x-www-form-urlencoded 타입 선언
  //     //     // },
  //     //   }) //here
  //     //   .then((res) => {
  //     //     console.log("loginProc is good");
  //     //     console.log(res.data);
  //     //   })
  //     //   .catch((err) => {
  //     //     console.log(err);
  //     //   });
  //   })
  //   .catch((err) => {
  //     console.log(err);
  //     // console.log("@@@@@@@@222what the ", playerSummariesPromise);
  //   });
}

/**
 * `Strategy` constructor.
 *
 * The Steam authentication strategy authenticates requests by delegating to
 * Steam using the OpenID 2.0 protocol.
 *
 * Applications must supply a `validate` callback which accepts an `identifier`,
 * and optionally a service-specific `profile`, and then calls the `done`
 * callback supplying a `user`, which should be set to `false` if the
 * credentials are not valid.  If an exception occured, `err` should be set.
 * '전략' 구축자.

 Steam 인증 전략은 Open을 사용하여 Steam에 위임하여 요청을 인증합니다ID 2.0 프로토콜입니다.

 응용 프로그램은 "식별자"와 선택적으로 서비스별 "프로파일"을 수락하는 "검증" 콜백을 제공한 다음 "완료" 콜백을 
 "사용자"에게 호출해야 하며, 이는 자격 증명이 유효하지 않을 경우 "거짓"으로 설정되어야 한다.  예외가 발생하면 err을 설정해야 합니다.
 *
 * Options:
 *   - `returnURL`  URL to which Steam will redirect the user after authentication (인증 후 Steam이 사용자를 리디렉션할 URL)
 *   - `realm`      the part of URL-space for which an OpenID authentication request is valid(열린 URL 공간 부분ID 인증 요청이 유효합니다)
 *   - `profile`    enable profile exchange, defaults to _true_
 *
 * Examples:
 *
 *     passport.use(new SteamStrategy({
 *         returnURL: 'http://localhost:3000/auth/steam/return',
 *         realm: 'http://localhost:3000/'
 *       },
 *       function(identifier, profile, done) {
 *         User.findByOpenID(identifier, function (err, user) {
 *           done(err, user);
 *         });
 *       }
 *     ));
 *
 * @param {Object} options
 * @param {Function} validate
 * @api public
 */
function Strategy(options, validate) {
  console.log("##strategy.js -> function Strategy");
  options = options || {};
  options.providerURL = options.providerURL || "https://steamcommunity.com/openid";
  options.profile = options.profile === undefined ? true : options.profile;
  options.stateless = true; //Steam only works as a stateless OpenID

  var originalPassReqToCallback = options.passReqToCallback;
  options.passReqToCallback = true; //Request needs to be verified

  function verify(req, identifier, profile, done) {
    var validOpEndpoint = "https://steamcommunity.com/openid/login";
    var identifierRegex = /^https?:\/\/steamcommunity\.com\/openid\/id\/(\d+)$/;

    if (req.query["openid.op_endpoint"] !== validOpEndpoint || !identifierRegex.test(identifier)) {
      return done(null, false, {
        message: "Claimed identity is invalid.",
      });
    }

    var steamID = identifierRegex.exec(identifier)[0];

    if (options.profile) {
      //Get user profile
      getUserProfileAndSendSpring(options.apiKey, steamID);
      getUserProfile(options.apiKey, steamID, function (err, profile) {
        if (err) {
          done(err);
        } else {
          if (originalPassReqToCallback) {
            validate(req, identifier, profile, done);
          } else {
            validate(identifier, profile, done);
          }
        }
      });
    } else {
      if (originalPassReqToCallback) {
        validate(req, identifier, profile, done);
      } else {
        validate(identifier, profile, done);
      }
    }
  }

  OpenIDStrategy.call(this, options, verify);

  this.name = "steam";
  this.stateless = options.stateless;
}

/**
 * Inherit from `OpenIDStrategy`.
 */
util.inherits(Strategy, OpenIDStrategy);

/**
 * Expose `Strategy`.
 */
module.exports = Strategy;
