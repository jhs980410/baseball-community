import React, { useContext, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import "./Mypage.css";

export default function Mypage() {
  const { userInfo } = useContext(AuthContext);
  const [activeTab, setActiveTab] = useState("posts");

  // ✅ 더미데이터
  const dummyPosts = [
    { id: 1, title: "오늘 경기 직관 후기", date: "2025-08-24", views: 152 },
    { id: 2, title: "김재윤 마무리 복귀 전망", date: "2025-08-23", views: 89 },
  ];

  const dummyComments = [
    { id: 1, content: "맞아요! 저도 그렇게 생각합니다.", postTitle: "오늘 경기 직관 후기", date: "2025-08-24" },
    { id: 2, content: "투수 교체가 좀 늦었죠.", postTitle: "김재윤 마무리 복귀 전망", date: "2025-08-23" },
  ];

  const dummyLikes = [
    { id: 1, title: "KT 위즈 타선 대폭발", date: "2025-08-22", author: "야구팬123" },
    { id: 2, title: "LG, 우승 가능성 있을까?", date: "2025-08-21", author: "베어스짱" },
  ];

  if (!userInfo) {
    return (
      <div className="mypage">
        <h2>마이페이지</h2>
        <p>로그인 후 이용 가능합니다.</p>
      </div>
    );
  }

  return (
    <div className="mypage">
      {/* 왼쪽 메뉴 */}
      <aside className="mypage-sidebar">
        <div className="profile">
          <div className="avatar">👤</div>
          <p className="nickname">{userInfo.nickname}</p>
          <p className="email">{userInfo.email}</p>
        </div>

        <ul className="menu">
          <li 
            className={activeTab === "posts" ? "active" : ""} 
            onClick={() => setActiveTab("posts")}
          >
            내가 쓴 글
          </li>
          <li 
            className={activeTab === "comments" ? "active" : ""} 
            onClick={() => setActiveTab("comments")}
          >
            내가 쓴 댓글
          </li>
          <li 
            className={activeTab === "likes" ? "active" : ""} 
            onClick={() => setActiveTab("likes")}
          >
            좋아요한 글
          </li>
          <li 
            className={activeTab === "settings" ? "active" : ""} 
            onClick={() => setActiveTab("settings")}
          >
            프로필 수정
          </li>
        </ul>
      </aside>

      {/* 오른쪽 콘텐츠 */}
      <section className="mypage-content">
        {activeTab === "posts" && (
          <div>
            <h3 className="mypagetitle">내가 쓴 글</h3>
            <ul>
              {dummyPosts.map((post) => (
                <li key={post.id}>
                  <strong>{post.title}</strong> 
                  <span> ({post.date}) </span> 
                  <span>조회수 {post.views}</span>
                </li>
              ))}
            </ul>
          </div>
        )}

        {activeTab === "comments" && (
          <div>
            <h3 className="mypagetitle">내가 쓴 댓글</h3>
            <ul>
              {dummyComments.map((c) => (
                <li key={c.id}>
                  <strong>{c.content}</strong>
                  <small>
                    → 원글: {c.postTitle} ({c.date})
                  </small>
                </li>
              ))}
            </ul>
          </div>
        )}

        {activeTab === "likes" && (
          <div>
            <h3 className="mypagetitle">좋아요한 글</h3>
            <ul>
              {dummyLikes.map((like) => (
                <li key={like.id}>
                  <strong>{like.title}</strong> 
                  <span> ({like.date}) </span>
                  <span>- 작성자: {like.author}</span>
                </li>
              ))}
            </ul>
          </div>
        )}

        {activeTab === "settings" && (
          <div>
            <h3 className="mypagetitle">프로필 수정</h3>
            <p>닉네임 / 비밀번호 변경 UI 들어갈 예정</p>
          </div>
        )}
      </section>
    </div>
  );
}
