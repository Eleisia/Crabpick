// 게임 데이터 타입 정의
type Game = {
  id: number;
  name: string;
  genre: string;
  releaseYear: number;
};

// 5개의 게임 데이터 생성
const games: Game[] = [
  {
    id: 1,
    name: "The Legend of Zelda: Breath of the Wild",
    genre: "Action-adventure",
    releaseYear: 2017,
  },
  {
    id: 2,
    name: "Super Mario Odyssey",
    genre: "Platformer",
    releaseYear: 2017,
  },
  {
    id: 3,
    name: "Red Dead Redemption 2",
    genre: "Action-adventure",
    releaseYear: 2018,
  },
  {
    id: 4,
    name: "God of War",
    genre: "Action-adventure",
    releaseYear: 2018,
  },
  {
    id: 5,
    name: "The Last of Us Part II",
    genre: "Action-adventure",
    releaseYear: 2020,
  },
];
