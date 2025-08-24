import React from "react";
import { Link } from "react-router-dom";
import { teams } from "../constants/teams";  // 10개 구단 상수
import "../styles/navbar.css";
export default function NavBar() {
  return (
    <nav className="navbar">
      <ul className="nav-menu">
        {teams.map((team) => (
          <li key={team.id}>
            <Link to={`/team/${team.id}`}>{team.name}</Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}
