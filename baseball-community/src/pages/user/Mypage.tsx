import React, { useContext, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../components/Pagination/Pagination";
import axios from "axios";
import { AuthContext } from "../../contexts/AuthContext";
import "./Mypage.css";

interface Post {
  id: number;
  title: string;
  content: string;
  nickname: string;
  createdAt: string;
  updatedAt: string;
  views?: number; // optional
}

interface Comment {
  id: number;
  content: string;
  postTitle: string;
  date: string;
}

interface Like {
  id: number;
  title: string;
  date: string;
  author: string;
}

export default function Mypage() {
  const { userInfo } = useContext(AuthContext);
  const [activeTab, setActiveTab] = useState("posts");

  //  각 탭별 상태
  const [posts, setPosts] = useState<Post[]>([]);
  const [comments, setComments] = useState<Comment[]>([]);
  const [likes, setLikes] = useState<Like[]>([]);

  //  페이지네이션 상태
  const [postPage, setPostPage] = useState(0);
  const [postTotalPages, setPostTotalPages] = useState(0);

  const [commentPage, setCommentPage] = useState(0);
  const [commentTotalPages, setCommentTotalPages] = useState(0);

  const [likePage, setLikePage] = useState(0);
  const [likeTotalPages, setLikeTotalPages] = useState(0);

  // 로딩 상태
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      if (!userInfo) return;
      setLoading(true);

      try {
        if (activeTab === "posts") {
          const res = await axios.get(
            `/api/posts/user/${userInfo.id}?page=${postPage}&size=10`
          );
          setPosts(res.data.content);
          setPostTotalPages(res.data.totalPages);
        }

        if (activeTab === "comments") {
          const res = await axios.get(
            `/api/comments/user/${userInfo.id}?page=${commentPage}&size=10`
          );
          setComments(res.data.content);
          setCommentTotalPages(res.data.totalPages);
        }

        if (activeTab === "likes") {
          const res = await axios.get(
            `/api/likes/user/${userInfo.id}?page=${likePage}&size=10`
          );
          setLikes(res.data.content);
          setLikeTotalPages(res.data.totalPages);
        }
      } catch (err) {
        console.error("마이페이지 데이터 로딩 실패:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [activeTab, postPage, commentPage, likePage, userInfo]);

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
        {loading && <p>로딩 중...</p>}

        {activeTab === "posts" && !loading && (
          <div>
            <h3 className="mypagetitle">내가 쓴 글</h3>
            <ul>
              {posts.map((post) => (
                <li key={post.id}>
                  <Link to={`/posts/${post.id}`}>
                    <strong>{post.title}</strong>
                  </Link>
                  <span>
                    {" "}
                    ({new Date(post.createdAt).toLocaleDateString()}){" "}
                  </span>
                  {post.views !== undefined && (
                    <span>조회수 {post.views}</span>
                  )}
                </li>
              ))}
            </ul>
            <Pagination
              currentPage={postPage}
              totalPages={postTotalPages}
              onPageChange={setPostPage}
            />
          </div>
        )}

        {activeTab === "comments" && !loading && (
          <div>
            <h3 className="mypagetitle">내가 쓴 댓글</h3>
            <ul>
              {comments.map((c) => (
                <li key={c.id}>
                  <strong>{c.content}</strong>
                  <small>
                    → 원글: {c.postTitle} ({c.date})
                  </small>
                </li>
              ))}
            </ul>
            <Pagination
              currentPage={commentPage}
              totalPages={commentTotalPages}
              onPageChange={setCommentPage}
            />
          </div>
        )}

        {activeTab === "likes" && !loading && (
          <div>
            <h3 className="mypagetitle">좋아요한 글</h3>
            <ul>
              {likes.map((like) => (
                <li key={like.id}>
                  <strong>{like.title}</strong>
                  <span> ({like.date}) </span>
                  <span>- 작성자: {like.author}</span>
                </li>
              ))}
            </ul>
            <Pagination
              currentPage={likePage}
              totalPages={likeTotalPages}
              onPageChange={setLikePage}
            />
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
