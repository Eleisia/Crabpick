import csv
import os


PATH = os.path.dirname(os.path.realpath(__file__))
file_list = ["part-r-00000"]
if not os.path.exists(f"{PATH}/avg_playtime.csv",):
    with open(f"{PATH}/avg_playtime.csv", "a", newline='', encoding="utf-8") as rlt_f:
        csv.writer(rlt_f).writerow(["appid", "avg_playtime"])
try:
    for avg_playtime_file in file_list:
        with open(f"{PATH}/data/game_playtime/{avg_playtime_file}", "r") as f:
            print(f"open {avg_playtime_file}")
            for line in f.readlines():
                if not line.strip():
                    continue
                try:
                    key, value = line.split("\t")
                    with open(f"{PATH}/data/avg_playtime.csv", "a", newline='', encoding="utf-8") as rlt_f:
                        csv.writer(rlt_f).writerow([key, value.strip()])
                except FileNotFoundError as e:
                    with open(f"{PATH}/error_log.txt", "a", encoding="utf-8") as f:
                        f.writelines([e , "avg_playtime.csv","\n"])
                        print(e)

except FileNotFoundError as e:
    with open(f"{PATH}/error_log.txt", "a", encoding="utf-8") as f:
            f.writelines([e ,"\n", avg_playtime_file,"\n"])
            print(e)
except Exception as e:
    with open(f"{PATH}/error_log.txt", "a", encoding="utf-8") as f:
            f.writelines([e ,"\n", avg_playtime_file,"\n"])
            print(e)
