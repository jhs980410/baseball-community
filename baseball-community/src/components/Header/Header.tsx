import React, { useState, useRef, useContext } from "react";
import { Link } from "react-router-dom";
import LoginModal from "../../pages/Login/LoginModal";
import useOutsideClick from "../../hooks/useOutsideClick";
import { AuthContext } from "../../contexts/AuthContext"; // 전역 로그인 상태
import "./Header.css";
import axios from "axios";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const loginRef = useRef<HTMLDivElement>(null);
  const { userInfo, setUserInfo } = useContext(AuthContext);

  useOutsideClick(loginRef, () => setShowLogin(false));

 const handleLogout = async () => {
  try {
    await axios.post("/api/auth/logout", {}, { withCredentials: true }); 
    // 서버에서 Refresh Token 삭제 + Access Token 블랙리스트 등록

    setUserInfo(null); // 프론트 상태 초기화
  } catch (error) {
    console.error("로그아웃 실패:", error);
  }
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
