import React, { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Pagination from "../../components/Pagination/Pagination";
import { teams } from "../../constants/teams";
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
  teamId?: string;     // 팀 ID (없으면 전체 게시판)
  searchType?: string;
  keyword?: string;
}

export default function Posts({ teamId, searchType, keyword }: PostsProps) {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [tab, setTab] = useState<"all" | "hot">("all"); // 전체 vs 인기글
  const { userInfo } = useContext(AuthContext);

  // 게시글 불러오기
  useEffect(() => {
    const fetchPosts = async () => {
      try {
        let url = "";

        if (tab === "hot") {
          // 인기글 API 호출 (팀별/전체 구분)
          url =
            teamId && teamId !== "all"
              ? `/api/posts/teams/${teamId}/hot?page=${page}&size=10`
              : `/api/posts/hot?page=${page}&size=10`;
        } else {
          // 전체글
          if (keyword && searchType) {
            url = `/api/posts?type=${searchType}&keyword=${keyword}&page=${page}&size=10`;
          } else if (teamId && teamId !== "all") {
            url = `/api/posts/teams/${teamId}?page=${page}&size=10`;
          } else {
            url = `/api/posts?page=${page}&size=10`;
          }
        }

        const res = await axios.get(url, { withCredentials: true });

        // Page 구조 반영
        setPosts(res.data.content);
        setTotalPages(res.data.totalPages);
      } catch (error) {
        console.error("게시글 로딩 실패:", error);
      }
    };

    fetchPosts();
  }, [teamId, searchType, keyword, page, tab]);

  // 팀명 표시
  const teamName =
    teamId && teamId !== "all"
      ? teams.find((t) => t.id === teamId)?.name ?? teamId
      : keyword
      ? `검색 결과 (${keyword})`
      : "전체";

  return (
    <section className="posts">
      {/* 제목 */}
      <h2 className="posts-title">{teamName} 게시판</h2>

      {/* 탭 + 글쓰기 버튼 */}
      <div className="posts-header">
        <div className="posts-tabs">
          <button
            className={tab === "all" ? "active" : ""}
            onClick={() => { setPage(0); setTab("all"); }}
          >
            전체글
          </button>
          <button
            className={tab === "hot" ? "active" : ""}
            onClick={() => { setPage(0); setTab("hot"); }}
          >
            인기글
          </button>
        </div>

        {userInfo && (
          <Link to="/posts/create" className="btn-create">
            ✍ 글쓰기
          </Link>
        )}
      </div>

      {/* 게시글 목록 */}
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

      {/* 페이징 */}
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />
    </section>
  );
}
