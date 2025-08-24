import React, { useState, useRef, useEffect } from "react";
import "../styles/Header.css";
import "../styles/LoginModal.css";
import LoginModal from "../pages/Login/LoginModal";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const loginRef = useRef<HTMLDivElement>(null);

  // 바깥 클릭 시 닫기
  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (loginRef.current && !loginRef.current.contains(e.target as Node)) {
        setShowLogin(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <header className="header">
      <div className="top-bar">
        <div className="logo">KBO 다모여라</div>
        <div className="search">
          <input type="text" placeholder="검색하기" />
        </div>

        {/* 로그인 버튼 + 드롭다운 */}
        <div className="login" ref={loginRef}>
          <button onClick={() => setShowLogin(!showLogin)}>로그인</button>
          {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
        </div>
      </div>
    </header>
  );
}
