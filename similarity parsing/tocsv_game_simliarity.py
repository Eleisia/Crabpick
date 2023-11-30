import csv
import os


PATH = os.path.dirname(os.path.realpath(__file__))
file_list = ["part-r-00000", "part-r-00001", "part-r-00002", "part-r-00003", "part-r-00004", "part-r-00005", "part-r-00006", "part-r-00007", "part-r-00008", "part-r-00009"]
if not os.path.exists(f"{PATH}/game_similarity2.csv",):
    with open(f"{PATH}/game_similarity2.csv", "a", newline='', encoding="utf-8") as rlt_f:
        csv.writer(rlt_f).writerow(["appid", "similarity"])
try:
    for similarity_file in file_list:
        with open(f"{PATH}/data/game_similarity2/{similarity_file}", "r") as f:
            for line in f.readlines():
                if not line.strip():
                    continue
                try:
                    key, value = line.split("\t")
                    with open(f"{PATH}/data/game_similarity2.csv", "a", newline='', encoding="utf-8") as rlt_f:
                        csv.writer(rlt_f).writerow([key, value.strip()])
                except FileNotFoundError as e:
                    with open(f"{PATH}/error_log.txt", "a", encoding="utf-8") as f:
                        f.writelines([e , "game_similarity2.csv","\n"])
                        print(e)

except FileNotFoundError as e:
    with open(f"{PATH}/error_log.txt", "a", encoding="utf-8") as f:
            f.writelines([e , similarity_file,"\n"])
            print(e)
