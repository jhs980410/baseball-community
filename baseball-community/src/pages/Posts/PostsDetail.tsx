// src/pages/Posts/PostDetail.tsx
import React from "react";
import "./PostDetail.css";

export default function PostDetail() {
  return (
    <main className="main-content post-detail">
      <div className="post-detail-container">
        <h2 className="post-title">게시글 제목</h2>
        <div className="post-meta">
          <span>작성자: 홍길동</span>
          <span>2025-08-25</span>
        </div>
        <div className="post-content">
          게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...
          게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...게시글 내용입니다...
        </div>

        <div className="comment-section">
          <h3>댓글</h3>

          <div className="comment">
            <div className="comment-author">
              사용자1 <span className="comment-date">2025-08-25</span>
            </div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
          </div>
             <div className="comment">
            <div className="comment-author">
              사용자1 <span className="comment-date">2025-08-25</span>
            </div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
            <div className="comment-content">좋은 글이네요!</div>
          </div>

          <form className="comment-form">
            <textarea placeholder="댓글을 입력하세요"></textarea>
            <button type="submit">댓글 작성</button>
          </form>
        </div>
      </div>
    </main>
  );
}
