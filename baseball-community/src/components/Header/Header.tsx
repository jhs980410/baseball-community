import React, { useState, useRef, useContext } from "react";
import { Link } from "react-router-dom";
import LoginModal from "../../pages/Login/LoginModal";
import useOutsideClick from "../../hooks/useOutsideClick";
import { AuthContext } from "../../contexts/AuthContext"; // 전역 로그인 상태
import "./Header.css";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const loginRef = useRef<HTMLDivElement>(null);
  const { userInfo, setUserInfo } = useContext(AuthContext);

  useOutsideClick(loginRef, () => setShowLogin(false));

  const handleLogout = () => {
    // 필요시 서버 로그아웃 API 호출 (쿠키 제거)
    setUserInfo(null);
  };

  return (
    <header className="header">
      <div className="top-bar">
        <div className="logo">
          <Link to="/">KBO 다모여라</Link>
        </div>

        <div className="search">
          <input type="text" placeholder="검색하기" />
        </div>

        {/* 로그인 / 로그아웃 UI 전환 */}
        <div className="login" ref={loginRef}>
          {userInfo ? (
            <>
            <div className="auth-controls">
              <span className="nickname">{userInfo.nickname} 님</span>
              <Link to="/mypage" className="btn">마이페이지</Link>
              <button className="btn logout" onClick={handleLogout}>로그아웃</button>
            </div>

            </>
          ) : (
            <>
            <div className="auth-controls">
              <button className="btn login" onClick={() => setShowLogin(!showLogin)}>로그인</button>
              </div>
              {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
            </>
          )}
        </div>
      </div>
    </header>
  );
}
