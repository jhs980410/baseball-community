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
  commentCount: number;
   likeCount: number;  
  viewCount: number;   
}

interface PostsProps {
  teamId?: string;       // 팀 ID (없으면 전체 게시판)
  searchType?: string;   // 검색 타입 (title, nickname, all)
  keyword?: string;      // 검색 키워드
}

export default function Posts({ teamId, searchType, keyword }: PostsProps) {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const { userInfo } = useContext(AuthContext);

useEffect(() => {
  const fetchPosts = async () => {
    try {
      let url = "";

      if (keyword && searchType) {
        // 검색 API → /api/posts?...
        url = `/api/posts?type=${searchType}&keyword=${keyword}&page=${page}&size=10`;
      } else if (teamId && teamId !== "all") {
        // 팀별 게시판 → /api/posts/teams/{teamId}
        url = `/api/posts/teams/${teamId}?page=${page}&size=10`;
      } else {
        // 전체 게시판
        url = `/api/posts?page=${page}&size=10`;
      }

      const res = await axios.get(url, { withCredentials: true });
      setPosts(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (error) {
      console.error("게시글 로딩 실패:", error);
    }
  };

  fetchPosts();
}, [teamId, searchType, keyword, page]);
  // 팀 ID → 팀 이름 매핑
  const teamName =
    teamId && teamId !== "all"
      ? teams.find((t) => t.id === teamId)?.name ?? teamId
      : keyword
      ? `검색 결과 (${keyword})`
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
          <span className="col-likes">추천</span>   
          <span className="col-views">조회</span>    
        </div>
        {posts.map((post, index) => (
          <Link to={`/posts/${post.id}`} key={post.id} className="post-row">
            <span className="post-index">{page * 10 + index + 1}</span>
            <span className="post-title">{post.title}</span>
            <span className="post-date">
              {new Date(post.createdAt).toLocaleDateString()}
            </span>
            <span className="post-comments">💬 {post.commentCount}</span>
            <span className="post-likes">👍 {post.likeCount}</span>    
            <span className="post-views">👁 {post.viewCount}</span>    
          </Link>
        ))}
      </div>

      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />
    </section>
  );
}
