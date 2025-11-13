import React, { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Pagination from "../../components/Pagination/Pagination";
import { teams } from "../../constants/teams";
import "./Posts.css";
import { AuthContext } from "../../contexts/AuthContext";

// ---------------------------
// 타입 정의
// ---------------------------
interface NoticeTop {
  id: number;
  title: string;
  createdAt: string;
  pinned: boolean;
  commentCount: number;
  likeCount: number;
  viewCount: number;
}

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
  teamId?: string;
  searchType?: string;
  keyword?: string;
}

// ---------------------------
// 컴포넌트
// ---------------------------
export default function Posts({ teamId, searchType, keyword }: PostsProps) {
  const { userInfo } = useContext(AuthContext);

  const [posts, setPosts] = useState<Post[]>([]);
  const [notices, setNotices] = useState<NoticeTop[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [tab, setTab] = useState<"all" | "hot" | "notice">("all");

  // ---------------------------
  // 데이터 불러오기
  // ---------------------------
  useEffect(() => {
    const fetchData = async () => {
      try {
        // 🔥 공지사항 탭 (전체 공지 리스트)
        if (tab === "notice") {
          const res = await axios.get("/api/notices", {
            params: { page, size: 10 },
          });

          setNotices(res.data.content);
          setTotalPages(res.data.totalPages);
          setPosts([]);
          return;
        }

        // 🔥 일반 탭 — 상단 고정 공지 2개
        const topNoticeRes = await axios.get("/api/notices/top");
        setNotices(topNoticeRes.data);

        // 🔥 게시글 URL 생성
        let url = "";
        if (tab === "hot") {
          url =
            teamId && teamId !== "all"
              ? `/api/posts/teams/${teamId}/hot?page=${page}&size=10`
              : `/api/posts/hot?page=${page}&size=10`;
        } else {
          if (keyword && searchType) {
            url = `/api/posts?type=${searchType}&keyword=${keyword}&page=${page}&size=10`;
          } else if (teamId && teamId !== "all") {
            url = `/api/posts/teams/${teamId}?page=${page}&size=10`;
          } else {
            url = `/api/posts?page=${page}&size=10`;
          }
        }

        const res = await axios.get(url, { withCredentials: true });
        setPosts(res.data.content);
        setTotalPages(res.data.totalPages);
      } catch (err) {
        console.error("게시글/공지 로딩 실패:", err);
      }
    };

    fetchData();
  }, [teamId, searchType, keyword, page, tab]);

  // ---------------------------
  // 팀명 표시
  // ---------------------------
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
            onClick={() => {
              setTab("all");
              setPage(0);
            }}
          >
            전체글
          </button>

          <button
            className={tab === "hot" ? "active" : ""}
            onClick={() => {
              setTab("hot");
              setPage(0);
            }}
          >
            인기글
          </button>

          <button
            className={tab === "notice" ? "active" : ""}
            onClick={() => {
              setTab("notice");
              setPage(0);
            }}
          >
            공지사항
          </button>
        </div>

        {userInfo && (
          <Link to="/posts/create" className="btn-create">
            ✍ 글쓰기
          </Link>
        )}
      </div>

      {/* 게시글 / 공지 목록 */}
      <div className="post-list">
        <div className="post-list-header">
          <span className="col-index">번호</span>
          <span className="col-title">제목</span>
          <span className="col-date">작성일</span>
          <span className="col-comments">댓글</span>
          <span className="col-likes">추천</span>
          <span className="col-views">조회</span>
        </div>

        {/* ------------------------------------
            🔥 공지사항 탭 — 전체 공지 리스트
        ------------------------------------ */}
        {tab === "notice" &&
          notices.map((notice, index) => (
            <Link
              to={`/notice/${notice.id}`}
              key={`notice-list-${notice.id}`}
              className="post-row notice-row"
            >
              <span className="post-index">{page * 10 + index + 1}</span>
              <span className="post-title">{notice.title}</span>
              <span className="post-date">
                {new Date(notice.createdAt).toLocaleDateString()}
              </span>
              <span className="post-comments">💬 {notice.commentCount}</span>
              <span className="post-likes">👍 {notice.likeCount}</span>
              <span className="post-views">👁 {notice.viewCount}</span>
            </Link>
          ))}

        {/* ------------------------------------
            🔥 일반 탭 — 상단 고정 공지
        ------------------------------------ */}
        {tab !== "notice" &&
          notices.map((notice) => (
            <Link
              to={`/notice/${notice.id}`}
              key={`notice-top-${notice.id}`}
              className="post-row notice-row"
            >
              <span className="post-index">📌</span>
              <span className="post-title">[공지] {notice.title}</span>
              <span className="post-date">
                {new Date(notice.createdAt).toLocaleDateString()}
              </span>
              <span className="post-comments">💬 {notice.commentCount}</span>
              <span className="post-likes">👍 {notice.likeCount}</span>
              <span className="post-views">👁 {notice.viewCount}</span>
            </Link>
          ))}

        {/* ------------------------------------
            🔥 일반 게시글
        ------------------------------------ */}
        {tab !== "notice" &&
          posts.map((post, index) => (
            <Link
              to={`/posts/${post.id}`}
              key={post.id}
              className="post-row"
            >
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
