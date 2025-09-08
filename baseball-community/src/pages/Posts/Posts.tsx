import React, { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Pagination from "../../components/Pagination/Pagination";
import { teams } from "../../constants/teams"; // 팀 상수 불러오기
import "./Posts.css";
import { AuthContext } from "../../contexts/AuthContext";

interface Post {
  id: number;
  title: string;
  userId: number;
  teamId: number;
  createdAt: string;
  updatedAt: string;
  commentCount:number;
}

interface PostsProps {
  teamId?: string;
}

export default function Posts({ teamId }: PostsProps) {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const { userInfo } = useContext(AuthContext);
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
     <div className="posts-header">
        <h2>{teamName} 게시판</h2>
            {userInfo && (
          <Link to="/posts/create" className="btn-create">
            ✍ 글쓰기
          </Link>
        )}
    </div>
      
     <div className="post-list">
  <div className="post-list-header">
    <span className="col-index">번호</span>
    <span className="col-title">제목</span>
    <span className="col-date">작성일</span>
    <span className="col-comments">댓글</span>
  </div>
  {posts.map((post, index) => (
    <Link to={`/posts/${post.id}`} key={post.id} className="post-row">
      <span className="post-index">{index + 1}</span>
      <span className="post-title">{post.title}</span>
      <span className="post-date">{new Date(post.createdAt).toLocaleDateString()}</span>
      <span className="post-comments">💬 {post.commentCount}</span>
    </Link>
  ))}
</div>

      <Pagination currentPage={page} totalPages={totalPages} onPageChange={setPage} />
    </section>
  );
}
