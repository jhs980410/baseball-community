import React from "react";
import "../styles/SideBar.css";

export default function SideBar() {
    
    return(
           <aside className="sidebar">
        <div>
            팀별
        </div>
          <h3>선수 스탯들</h3>
          <div className="stat-box">선수 1</div>
          <div className="stat-box">선수 2</div>
          <div className="stat-box">선수 3</div>
          <div className="stat-box">선수 4</div>
          <div className="stat-box">선수 5</div>
        </aside>
    )
}