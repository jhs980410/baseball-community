import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import LoginModal from "../../pages/Login/LoginModal";
import useOutsideClick from "../../hooks/useOutsideClick";
import "./Header.css";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const loginRef = useRef<HTMLDivElement>(null);

  // 커스텀 훅 사용 (loginRef로 교체)
  useOutsideClick(loginRef, () => setShowLogin(false));

  return (
    <header className="header">
      <div className="top-bar">
        <div className="logo">
          <Link to="/">KBO 다모여라</Link>
        </div>
        <div className="search">
          <input type="text" placeholder="검색하기" />
        </div>

        {/* 로그인 버튼 */}
        <div className="login" ref={loginRef}>
          <button onClick={() => setShowLogin(!showLogin)}>로그인</button>
          {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
        </div>
      </div>
    </header>
  );
}
