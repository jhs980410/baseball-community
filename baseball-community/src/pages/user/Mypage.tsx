// src/pages/user/Mypage.tsx
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
  views?: number;
}

interface Comment {
  id: number;
  content: string;
  date: string;
  postId: number;
  postTitle: string;
}

interface Like {
  postId: number;
  title: string;
  date: string;
  author: string;
}

export default function Mypage() {
  const { userInfo } = useContext(AuthContext);
  const [activeTab, setActiveTab] = useState("posts");

  // 데이터 상태
  const [posts, setPosts] = useState<Post[]>([]);
  const [comments, setComments] = useState<Comment[]>([]);
  const [likes, setLikes] = useState<Like[]>([]);

  // 페이지네이션 상태
  const [postPage, setPostPage] = useState(0);
  const [postTotalPages, setPostTotalPages] = useState(0);

  const [commentPage, setCommentPage] = useState(0);
  const [commentTotalPages, setCommentTotalPages] = useState(0);

  const [likePage, setLikePage] = useState(0);
  const [likeTotalPages, setLikeTotalPages] = useState(0);

  // 로딩 상태
  const [loading, setLoading] = useState(false);

  // ---------------- 프로필 수정 관련 ----------------
  const [verified, setVerified] = useState(false); // 비밀번호 인증 여부
  const [currentPassword, setCurrentPassword] = useState("");
  const [newNickname, setNewNickname] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  // 좋아요 토글
  const handleToggleLike = async (postId: number) => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      return;
    }
    try {
      const res = await axios.post(
        `/api/likes/${postId}/user/${userInfo.id}/toggle`
      );
      const data = res.data;
      if (!data.likedByCurrentUser) {
        setLikes((prev) => prev.filter((p) => p.postId !== postId));
      }
    } catch (err) {
      console.error("좋아요 토글 실패:", err);
    }
  };

  // 데이터 불러오기
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

  // ---------------- 이벤트 핸들러 ----------------

  // 1. 기존 비밀번호 확인
  const handleVerifyPassword = async () => {
    try {
      await axios.post(`/api/auth/verify-password`, {
        userId: userInfo?.id,
        password: currentPassword,
      });
      setVerified(true);
      alert("비밀번호 확인 성공! 이제 프로필을 수정할 수 있습니다.");
    } catch {
      alert("비밀번호가 올바르지 않습니다.");
    }
  };

  // 2. 닉네임 중복 확인
  const handleCheckNickname = async () => {
    try {
      const res = await axios.get(
        `/api/auth/check-nickname?nickname=${newNickname}`
      );
      if (res.data) {
        alert("사용 가능한 닉네임입니다.");
      } else {
        alert("이미 사용 중인 닉네임입니다.");
      }
    } catch (err) {
      console.error("닉네임 중복 확인 실패:", err);
    }
  };

  // 3. 프로필 저장
  const handleSaveProfile = async () => {
    if (newPassword && newPassword !== confirmPassword) {
      alert("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      await axios.put(`/api/users/me/${userInfo?.id}`, {
        nickname: newNickname || userInfo?.nickname,
        password: newPassword || undefined,
      });
      alert("프로필이 수정되었습니다.");
      setVerified(false); // 다시 인증 필요
      setNewNickname("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (err) {
      console.error("프로필 수정 실패:", err);
      alert("프로필 수정 중 오류가 발생했습니다.");
    }
  };

  // ---------------- 렌더링 ----------------
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
      {/* 왼쪽 사이드바 */}
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
           <li
            className={activeTab === "delete" ? "active" : ""}
            onClick={() => setActiveTab("delete")}
          >
            회원 탈퇴
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
                  <Link to={`/posts/${post.id}`} className="post-link">
                    <strong>{post.title}</strong>
                    <span>({new Date(post.createdAt).toLocaleDateString()})</span>
                  </Link>
                  {post.views !== undefined && (
                    <span className="views">조회수 {post.views}</span>
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
                <li key={c.id} className="comment-item">
                  <div className="comment-left">
                    <Link to={`/posts/${c.postId}`} className="comment-link">
                      <strong>{c.content}</strong>
                    </Link>
                  </div>
                  <div className="comment-right">
                    <small>
                      원글: {c.postTitle} ({new Date(c.date).toLocaleDateString()}
                      )
                    </small>
                  </div>
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
                <li key={like.postId} className="like-item">
                  <Link to={`/posts/${like.postId}`} className="like-link">
                    <div className="like-left">
                      <strong>{like.title}</strong>
                    </div>
                    <div className="like-right">
                      <span>{new Date(like.date).toLocaleDateString()}</span>
                      <span className="like-author">
                        {" "}
                        - 작성자: {like.author}
                      </span>
                    </div>
                  </Link>
                  <button
                    className="btn-unlike"
                    onClick={() => handleToggleLike(like.postId)}
                  >
                    좋아요 취소
                  </button>
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
          <div className="settings">
            <h3 className="mypagetitle">프로필 수정</h3>

            {!verified ? (
              // 기존 비밀번호 확인
              <div className="verify-password">
                <label>비밀번호 확인</label>
                <input
                  type="password"
                  placeholder="현재 비밀번호 입력"
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                />
                <button onClick={handleVerifyPassword}>확인</button>
              </div>
            ) : (
              //  수정 가능 UI
              <div className="edit-profile">
                {/* 닉네임 변경 */}
                <div className="form-group">
                  <label>닉네임 변경</label>
                  <input
                    type="text"
                    placeholder="새 닉네임 입력"
                    value={newNickname}
                    onChange={(e) => setNewNickname(e.target.value)}
                  />
                  <button onClick={handleCheckNickname}>중복 확인</button>
                </div>

                {/* 비밀번호 변경 */}
                <div className="form-group">
                  <label>비밀번호 변경</label>
                  <input
                    type="password"
                    placeholder="새 비밀번호"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                  <input
                    type="password"
                    placeholder="새 비밀번호 확인"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                </div>

                <button onClick={handleSaveProfile}>저장하기</button>
              </div>
            )}
          </div>
          
        )}
        {activeTab === "delete" && (
  <div className="delete-account">
    <h3 className="mypagetitle">회원 탈퇴</h3>

    <p className="warning">
      ⚠ 회원 탈퇴 시 모든 데이터가 삭제되며 복구할 수 없습니다. 
      정말 탈퇴하시겠습니까?
    </p>

    <div className="form-group">
      <label>비밀번호 확인</label>
      <input
        type="password"
        placeholder="비밀번호 입력"
        value={currentPassword}
        onChange={(e) => setCurrentPassword(e.target.value)}
      />
    </div>

    <button
      className="btn-delete"
      onClick={async () => {
        try {
          await axios.delete("/api/users/me", {
                data: { password: currentPassword },
                withCredentials: true,   //  쿠키 전송 허용
              });
          alert("회원 탈퇴가 완료되었습니다.");
          // 로그아웃 처리 or 메인 페이지 이동
          window.location.href = "/";
        } catch (err) {
          console.error("회원 탈퇴 실패:", err);
          alert("회원 탈퇴 중 오류가 발생했습니다.");
        }
      }}
    >
      회원 탈퇴
    </button>
  </div>
)}

      </section>
    </div>
  );
}
