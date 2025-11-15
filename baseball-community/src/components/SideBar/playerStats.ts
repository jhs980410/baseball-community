export interface PlayerInfo {
  name: string;
  team: string;
  type: "pitcher" | "hitter" | "star";
  position: string;

  // hitter stats
  avg?: number;
  hr?: number;
  rbi?: number;

  // pitcher stats
  era?: number;
  win?: number;
  strikeout?: number;
  sv?: number;

  image: string;
}

export const teamPlayers: Record<string, PlayerInfo[]> = {
  LG: [
    {
      name: "손주영",
      team: "LG",
      type: "pitcher",
      position: "SP",
      era: 3.58,
      win: 9,
      strikeout: 112,
      image: "/players/sonjuyoung.png",
    },
    {
      name: "오스틴 딘",
      team: "LG",
      type: "hitter",
      position: "1B/RF",
      avg: 0.289,
      hr: 23,
      rbi: 95,
      image: "/players/austindean.png",
    },
    {
      name: "김현수",
      team: "LG",
      type: "star",
      position: "LF",
      avg: 0.285,
      hr: 12,
      rbi: 68,
      image: "/players/kimhyunsu.png",
    },
  ],

  KT: [
    {
      name: "소형준",
      team: "KT",
      type: "pitcher",
      position: "SP",
      era: 3.43,
      win: 13,
      strikeout: 124,
      image: "/players/sohyoungjun.png",
    },
    {
      name: "강백호",
      team: "KT",
      type: "hitter",
      position: "1B/OF",
      avg: 0.307,
      hr: 18,
      rbi: 78,
      image: "/players/kangbaekho.png",
    },
    {
      name: "박영현",
      team: "KT",
      type: "star",
      position: "RP",
      era: 2.45,
      sv: 32,
      strikeout: 82,
      image: "/players/parkyounghyun.png",
    },
  ],

  DOOSAN: [
    {
      name: "곽빈",
      team: "DOOSAN",
      type: "pitcher",
      position: "SP",
      era: 3.16,
      win: 12,
      strikeout: 134,
      image: "/players/gwakbin.png",
    },
    {
      name: "제이크 케이브",
      team: "DOOSAN",
      type: "hitter",
      position: "OF",
      avg: 0.276,
      hr: 17,
      rbi: 63,
      image: "/players/jakecave.png",
    },
    {
      name: "양의지",
      team: "DOOSAN",
      type: "star",
      position: "C",
      avg: 0.301,
      hr: 16,
      rbi: 78,
      image: "/players/yangyiji.png",
    },
  ],

  KIA: [
    {
      name: "네일",
      team: "KIA",
      type: "pitcher",
      position: "SP",
      era: 2.93,
      win: 15,
      strikeout: 148,
      image: "/players/neil.png",
    },
    {
      name: "패트릭 위즈덤",
      team: "KIA",
      type: "hitter",
      position: "3B/1B",
      avg: 0.268,
      hr: 22,
      rbi: 81,
      image: "/players/wisdom.png",
    },
    {
      name: "김도영",
      team: "KIA",
      type: "star",
      position: "3B",
      avg: 0.307,
      hr: 20,
      rbi: 68,
      image: "/players/kimdoyoung.png",
    },
  ],

  SSG: [
    {
      name: "드류 앤더슨",
      team: "SSG",
      type: "pitcher",
      position: "SP",
      era: 3.21,
      win: 10,
      strikeout: 122,
      image: "/players/drewanderson.png",
    },
    {
      name: "최정",
      team: "SSG",
      type: "hitter",
      position: "3B",
      avg: 0.266,
      hr: 29,
      rbi: 87,
      image: "/players/choijeong.png",
    },
    {
      name: "한유섬",
      team: "SSG",
      type: "star",
      position: "OF",
      avg: 0.278,
      hr: 14,
      rbi: 63,
      image: "/players/hanyuseom.png",
    },
  ],

  HANWHA: [
    {
      name: "폰세",
      team: "HANWHA",
      type: "pitcher",
      position: "SP",
      era: 3.74,
      win: 8,
      strikeout: 131,
      image: "/players/ponce.png",
    },
    {
      name: "노시환",
      team: "HANWHA",
      type: "hitter",
      position: "3B",
      avg: 0.281,
      hr: 31,
      rbi: 101,
      image: "/players/noshihwan.png",
    },
    {
      name: "류현진",
      team: "HANWHA",
      type: "star",
      position: "SP",
      era: 3.45,
      win: 9,
      strikeout: 98,
      image: "/players/ryuhyunjin.png",
    },
  ],

  SAMSUNG: [
    {
      name: "후라도",
      team: "SAMSUNG",
      type: "pitcher",
      position: "SP",
      era: 3.51,
      win: 12,
      strikeout: 140,
      image: "/players/jurado.png",
    },
    {
      name: "구자욱",
      team: "SAMSUNG",
      type: "hitter",
      position: "RF",
      avg: 0.312,
      hr: 14,
      rbi: 82,
      image: "/players/gujauwook.png",
    },
    {
      name: "원태인",
      team: "SAMSUNG",
      type: "star",
      position: "SP",
      era: 3.65,
      win: 10,
      strikeout: 111,
      image: "/players/wontaeein.png",
    },
  ],

  NC: [
    {
      name: "신민혁",
      team: "NC",
      type: "pitcher",
      position: "SP",
      era: 3.92,
      win: 10,
      strikeout: 101,
      image: "/players/shinminhyuk.png",
    },
    {
      name: "박민우",
      team: "NC",
      type: "hitter",
      position: "2B",
      avg: 0.301,
      hr: 5,
      rbi: 48,
      image: "/players/parkminwoo.png",
    },
    {
      name: "박건우",
      team: "NC",
      type: "star",
      position: "RF",
      avg: 0.298,
      hr: 10,
      rbi: 62,
      image: "/players/parkgunwoo.png",
    },
  ],

  LOTTE: [
    {
      name: "박세웅",
      team: "LOTTE",
      type: "pitcher",
      position: "SP",
      era: 3.86,
      win: 8,
      strikeout: 125,
      image: "/players/parksewoong.png",
    },
    {
      name: "전준우",
      team: "LOTTE",
      type: "hitter",
      position: "LF",
      avg: 0.276,
      hr: 15,
      rbi: 74,
      image: "/players/jeonjunwoo.png",
    },
    {
      name: "김원중",
      team: "LOTTE",
      type: "star",
      position: "RP",
      era: 2.78,
      sv: 35,
      strikeout: 84,
      image: "/players/kimwonjoong.png",
    },
  ],

  KIWOOM: [
    {
      name: "알칸타라",
      team: "KIWOOM",
      type: "pitcher",
      position: "SP",
      era: 3.65,
      win: 12,
      strikeout: 136,
      image: "/players/alcantara.png",
    },
    {
      name: "최주환",
      team: "KIWOOM",
      type: "hitter",
      position: "2B",
      avg: 0.285,
      hr: 13,
      rbi: 59,
      image: "/players/choijuhwan.png",
    },
    {
      name: "송성문",
      team: "KIWOOM",
      type: "star",
      position: "3B",
      avg: 0.271,
      hr: 11,
      rbi: 53,
      image: "/players/songseongmun.png",
    },
  ],
};
