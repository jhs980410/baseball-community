import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Pagination from "../../components/Pagination/Pagination";
import { teams } from "../../constants/teams"; // 팀 상수 불러오기
import "./Posts.css";

interface Post {
  id: number;
  title: string;
  userId: number;
  teamId: number;
  createdAt: string;
  updatedAt: string;
}

interface PostsProps {
  teamId?: string;
}

export default function Posts({ teamId }: PostsProps) {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const url =
          teamId && teamId !== "all"
            ? `/api/posts/team/${teamId}?page=${page}&size=10`
            : `/api/posts?page=${page}&size=10`;

        const res = await axios.get(url);
        setPosts(res.data.content);
        setTotalPages(res.data.totalPages);
      } catch (error) {
        console.error("게시글 로딩 실패:", error);
      }
    };

    fetchPosts();
  }, [teamId, page]);

  // teamId → 팀 이름 매핑
  const teamName =
    teamId && teamId !== "all"
      ? teams.find((t) => t.id === teamId)?.name ?? teamId
      : "전체";

  return (
    <section className="posts">
      <h2>{teamName} 게시판</h2>
      <div className="post-list">
        {posts.map((post) => (
          <Link to={`/posts/${post.id}`} key={post.id} className="post-item">
            <div className="post-title">{post.title}</div>
            <div className="post-meta">
              <span className="date">{post.createdAt}</span>
            </div>
          </Link>
        ))}
      </div>
      <Pagination currentPage={page} totalPages={totalPages} onPageChange={setPage} />
    </section>
  );
}
