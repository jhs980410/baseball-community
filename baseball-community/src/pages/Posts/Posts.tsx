// src/pages/Posts/Posts.tsx
import React from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination/Pagination";
import "./Posts.css";

interface Post {
  id: number;
  title: string;
  author: string;
  createdAt: string;
  views: number;
  comments: number;
}

interface PostsProps {
  posts?: Post[];
  teamId?: string; // 팀별 게시판이면 전달
}

export default function Posts({ posts, teamId }: PostsProps) {
  // 더미 데이터 (API 연동 전용)
  const dummyPosts: Post[] = [
    {
      id: 1,
      title: teamId ? `${teamId} 팀 글 예시` : "전체 게시판 글",
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
  ];

  const postData = posts ?? dummyPosts;

  return (
    <section className="posts">
      <h2>{teamId ? `${teamId.toUpperCase()} 게시판` : "게시글 목록"}</h2>
      <div className="post-list">
        {postData.map((post) => (
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
