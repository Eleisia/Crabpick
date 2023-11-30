import csv
import os
import time

import requests

# def makeapplist():

#     appid = list()
#     name = list()

#     r = requests.get("https://api.steampowered.com/ISteamApps/GetAppList/v2/").json()
#     for app in r["applist"]["apps"]:
#         if app["name"].strip():
#             appid.append(int(app["appid"]))
#             name.append(app["name"])

#     data = pd.DataFrame()
#     data["appid"] = appid
#     data["name"] = name

#     data.to_csv("./applist.csv", encoding="utf-8-sig", index=False)

roooooot = os.path.dirname(__file__)


def makegames():
    if not os.path.exists(f"{roooooot}/games.csv"):
        with open(f"{roooooot}/games.csv", "a", newline='', encoding="utf-8") as games:
            csv_writer = csv.writer(games)
            csv_writer.writerow([None, "appid","name","age_limit","developers","genre","release","avg_playtime","mood","word_cloud","steam_link","image_link","trailer_link"])
    try:
        with open(f"{roooooot}/applist.csv", "r", encoding="utf-8") as f:
            with open(f"{roooooot}/games.csv", "a", newline='', encoding="utf-8") as ff:
                if os.path.exists(f"{roooooot}/error_log.txt"):
                    with open(f"{roooooot}/error_log.txt", "r", encoding="utf-8") as log:
                        log_lines = log.readlines()
                        applist_line, games_line = map(int, log_lines[-1].split())
                else:
                    applist_line = 1
                    games_line = 1
                start_line = applist_line
                applist = csv.reader(f)
                while start_line:
                    next(applist)   # 첫 줄 스킵
                    start_line -= 1
                for appid in applist:
                    start = time.time()
                    r = requests.get(f"https://store.steampowered.com/api/appdetails?appids={appid[0]}")
                    if r.status_code == 200:
                        try:
                            apps = r.json()
                        except:
                            with open(f"{roooooot}/error_log.txt", "a", encoding="utf-8") as f:
                                f.writelines([f"JSON Decode Error {appid} /n"])
                            continue
                        for app in apps.values():
                            if app["success"]: # 요청 성공 실패 여부
                                data = app["data"]
                                if data["type"].strip() == "game": # 게임 정보만 저장
                                    name = data["name"]
                                    age_limit = data["required_age"]
                                    if "developers" in data.keys(): 
                                        developers = data["developers"]
                                    else:
                                        developers = None
                                    if "genres" in data.keys(): 
                                        genre = data["genres"]
                                    else:
                                        genre = None
                                    release = data["release_date"]["date"]
                                    steam_link = f"https://store.steampowered.com/app/{appid[0]}"
                                    if "screenshots" in data.keys(): 
                                        image_link = data["screenshots"]
                                    else:
                                        image_link = None
                                    if "movies" in data.keys(): 
                                        trailer_link = data["movies"]
                                    else:
                                        trailer_link = None
                                    # csv 쓰기
                                    games = csv.writer(ff)
                                    games.writerow([games_line, appid[0],name, age_limit, developers, genre, release, None, None, None, steam_link, image_link, trailer_link])
                                    print(games_line)
                                    games_line += 1
                        # 시간 딜레이 하기
                        end = time.time()
                        gap = 1.5 - end + start
                        if gap > 0 :
                            time.sleep(gap) 
                        applist_line += 1
                    elif r.status_code == 429:
                        print("API Fail")
                        time.sleep(300)
                    elif r.status_code > 500:
                        print("Server Error")
                        time.sleep(1200)
    except KeyboardInterrupt as e:
        with open(f"{roooooot}/error_log.txt", "a", encoding="utf-8") as f:
            f.writelines(["Manualy shutdown" + "\n", f"{applist_line} {games_line}", "\n"])
            print(e)
    except Exception as e:
        with open(f"{roooooot}/error_log.txt", "a", encoding="utf-8") as f:
            f.writelines([e.__str__() + "\n", f"{applist_line} {games_line}", "\n"])
            print(e)


makegames()
