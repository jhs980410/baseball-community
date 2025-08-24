import React from "react"
import Pagination from "../../components/Pagination"
import "../../styles/Posts.css";

export default function Posts() {
  return (
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
      <Pagination />
    </section>
  );
}