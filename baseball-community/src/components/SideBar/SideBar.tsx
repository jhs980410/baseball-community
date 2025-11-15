import React, { useState } from "react";
import "./SideBar.css";
import { teamPlayers } from "./playerStats";
import type { PlayerInfo } from "./playerStats";

export default function SideBar() {
  const teams = Object.keys(teamPlayers);
  const filters = ["all", "pitcher", "hitter", "star"] as const;

  const [teamIndex, setTeamIndex] = useState(0);
  const selectedTeam = teams[teamIndex];

  const [filter, setFilter] = useState<(typeof filters)[number]>("all");

  const players = teamPlayers[selectedTeam].filter((p) =>
    filter === "all" ? true : p.type === filter
  );

  const prevTeam = () =>
    setTeamIndex((prev) => (prev === 0 ? teams.length - 1 : prev - 1));

  const nextTeam = () =>
    setTeamIndex((prev) => (prev === teams.length - 1 ? 0 : prev + 1));

  return (
    <aside className="sidebar">
      <h2 className="sidebar-title">팀별 주요 선수들</h2>

      {/* 팀 슬라이더 */}
      <div className="team-slider">
        <button className="arrow-btn" onClick={prevTeam}>
          ◀
        </button>

        <span className="team-name-display">{selectedTeam}</span>

        <button className="arrow-btn" onClick={nextTeam}>
          ▶
        </button>
      </div>

      {/* 필터 */}
      <div className="filter-box">
        {filters.map((f) => (
          <button
            key={f}
            className={`filter-btn ${filter === f ? "active" : ""}`}
            onClick={() => setFilter(f)}
          >
            {f === "all"
              ? "전체"
              : f === "pitcher"
              ? "투수"
              : f === "hitter"
              ? "타자"
              : "간판"}
          </button>
        ))}
      </div>

      {/* 선수 카드 */}
      <div className="player-list">
        {players.map((p, idx) => (
          <div key={idx} className="player-card">
            <img src={p.image} alt={p.name} className="player-img" />

            <div className="player-info">
              <div className="player-name">{p.name}</div>
              <div className="player-pos">
                {p.team} · {p.position}
              </div>

              {/* Pitcher or Star-Pitcher */}
              {(p.type === "pitcher" ||
                (p.type === "star" && p.era !== undefined)) && (
                <div className="player-stats">
                  <span>ERA {p.era}</span>
                  <span>W {p.win}</span>
                  <span>K {p.strikeout}</span>
                  {p.sv !== undefined && <span>SV {p.sv}</span>}
                </div>
              )}

              {/* Hitter or Star-Hitter */}
              {(p.type === "hitter" ||
                (p.type === "star" && p.avg !== undefined)) && (
                <div className="player-stats">
                  <span>AVG {p.avg}</span>
                  <span>HR {p.hr}</span>
                  <span>RBI {p.rbi}</span>
                </div>
              )}

              {/* Star label */}
              {p.type === "star" && (
                <div className="player-stats star-box">⭐ 팀 간판 선수</div>
              )}
            </div>
          </div>
        ))}
      </div>
    </aside>
  );
}
