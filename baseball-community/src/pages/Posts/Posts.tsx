// src/pages/Posts/Posts.tsx
import React from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination/Pagination";
import "./Posts.css";

const dummyPosts = [
  {
    id: 1,
    title: "김재윤 마무리 욕하던 집단들 현재 뭐하고 있을지",
    author: "서장우",
    createdAt: "2025-08-24 14:07",
    views: 418,
    comments: 3,
  },
  {
    id: 2,
    title: "마무리투수가 정말 중요하다는 걸 새삼 느낍니다",
    author: "미련임마시블",
    createdAt: "2025-08-24 09:52",
    views: 698,
    comments: 2,
  },
  {
    id: 3,
    title: "마무리투수가 정말 중요하다는 걸 새삼 느낍니다",
    author: "미련임마시블",
    createdAt: "2025-08-24 09:52",
    views: 698,
    comments: 2,
  },
  {
    id: 4,
    title: "마무리투수가 정말 중요하다는 걸 새삼 느낍니다",
    author: "미련임마시블",
    createdAt: "2025-08-24 09:52",
    views: 698,
    comments: 2,
  },
];

export default function Posts() {
  return (
    <section className="posts">
      <h2>게시글 목록</h2>
      <div className="post-list">
        {dummyPosts.map((post) => (
          <Link to={`/posts/${post.id}`} key={post.id} className="post-item">
            <div className="post-title">{post.title}</div>
            <div className="post-meta">
              <span className="author">{post.author}</span>
              <span className="date">{post.createdAt}</span>
              <span className="views">조회 {post.views}</span>
              <span className="comments">💬 {post.comments}</span>
            </div>
          </Link>
        ))}
      </div>

      {/* 페이지네이션 */}
      <Pagination />
    </section>
  );
}
