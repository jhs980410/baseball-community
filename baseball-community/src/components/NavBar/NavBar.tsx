import React from "react";
import { Link } from "react-router-dom";
import { teams } from "../../constants/teams";  // 10개 구단 상수
import "./navbar.css";

export default function NavBar() {
  return (
    <nav className="navbar">
      <ul className="nav-menu">
        {teams.map((team) => (
          <li key={team.id}>
            {/* 기존: /team/${team.id} → 변경: /teams/${team.id} */}
            <Link to={`/teams/${team.id}`}>{team.name}</Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}
