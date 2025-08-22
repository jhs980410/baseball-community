import React from "react";
import "../styles/Home.css";


export default function Home() {
  return (
    <div className="home-container">
      {/* 헤더 */}
      <header className="header">
        <div className="logo">로고</div>
        <div className="search">검색창</div>
        <div className="login">로그인</div>
      </header>

      {/* 메인 */}
      <main className="main-content">
        {/* 게시글 목록 */}
        <section className="posts">
          <h2>게시글 목록</h2>
          <div className="post-list">
            {Array.from({ length: 10 }).map((_, idx) => (
              <div key={idx} className="post-item">
                게시글 {idx + 1}
              </div>
            ))}
          </div>

          {/* 페이지네이션 */}
          <div className="pagination">
            <button>&lt;</button>
            {[1, 2, 3, 4, 5].map((num) => (
              <button key={num}>{num}</button>
            ))}
            <button>&gt;</button>
          </div>
        </section>

        {/* 사이드바 */}
        <aside className="sidebar">
          <h3>선수 스탯들</h3>
          <div className="stat-box">선수 1</div>
          <div className="stat-box">선수 2</div>
        </aside>
      </main>
    </div>
  );
}
