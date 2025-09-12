import React, { useState, useRef, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import LoginModal from "../../pages/Login/LoginModal";
import useOutsideClick from "../../hooks/useOutsideClick";
import { AuthContext } from "../../contexts/AuthContext";
import "./Header.css";
import axios from "axios";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const loginRef = useRef<HTMLDivElement>(null);
  const { userInfo, setUserInfo } = useContext(AuthContext);

  // 검색 상태
  const [keyword, setKeyword] = useState("");
  const [searchType, setSearchType] = useState("all");
  const navigate = useNavigate();

  useOutsideClick(loginRef, () => setShowLogin(false));

  // 로그아웃
  const handleLogout = async () => {
    try {
      await axios.post("/api/auth/logout", {}, { withCredentials: true });
      setUserInfo(null);
      navigate("/"); // 로그아웃 후 메인으로
    } catch (error) {
      console.error("로그아웃 실패:", error);
    }
  };

  // 🔎 검색 실행
  const handleSearch = () => {
    if (!keyword.trim()) return;
    navigate(`/search?type=${searchType}&keyword=${encodeURIComponent(keyword)}`);
    // Posts 컴포넌트에서 /api/posts?type=...&keyword=... 호출
  };

  return (
    <header className="header">
      <div className="top-bar">
        <div className="logo">
          <Link to="/">KBO 다모여라</Link>
        </div>

        {/* 검색창 */}
        <div className="search-bar">
          <select
            className="search-type"
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
          >
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="nickname">작성자</option>
          </select>

          <input
            type="text"
            placeholder="검색어를 입력하세요"
            className="search-input"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSearch()}
          />

          <button className="search-btn" onClick={handleSearch}>
            🔍
          </button>
        </div>

        {/* 로그인 / 로그아웃 */}
        <div className="login" ref={loginRef}>
          {userInfo ? (
            <div className="auth-controls">
              <span className="nickname">{userInfo.nickname} 님</span>
              <Link to="/mypage" className="btn">마이페이지</Link>
              <button className="btn logout" onClick={handleLogout}>
                로그아웃
              </button>
            </div>
          ) : (
            <div className="auth-controls">
              <button
                className="btn login"
                onClick={() => setShowLogin(!showLogin)}
              >
                로그인
              </button>
              {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
