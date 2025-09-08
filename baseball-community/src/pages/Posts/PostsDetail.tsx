import React, { useEffect, useState, useContext } from "react";

import { AuthContext } from "../../contexts/AuthContext"; // 로그인 유저 Context
import "./PostDetail.css";
import { Link, useParams, useNavigate } from "react-router-dom";
interface Comment {
  id: number;
  content: string;
  userNickname: string;
  createdAt: string;
}

interface Post {
  id: number;
  userId: number;
  title: string;
  content: string;
  nickname: string;
  createdAt: string;
  commentCount: number;
  comments: Comment[];
  likeCount: number;            // 좋아요 수
  likedByCurrentUser: boolean;  // 현재 유저가 눌렀는지 여부
}

export default function PostDetail() {
  const { post_id } = useParams<{ post_id: string }>();
  const { userInfo } = useContext(AuthContext);
  const [post, setPost] = useState<Post | null>(null);
  const [newComment, setNewComment] = useState("");

  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);
  const navigate = useNavigate();
  // 게시글 + 댓글 불러오기
  const fetchPost = () => {
    if (!post_id) return;
    fetch(`http://localhost:8080/api/posts/${post_id}?userId=${userInfo?.id || ""}`)
      .then((res) => res.json())
      .then((data) => {
        setPost(data);
        setLikeCount(data.likeCount);
        setLiked(data.likedByCurrentUser);
          })
      .catch((err) => console.error("게시글 불러오기 실패:", err));
  };

  useEffect(() => {
    fetchPost();
  }, [post_id]);

  // 댓글 작성
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim() || !post_id) return;

    if (!userInfo) {
      alert("로그인 후 댓글 작성이 가능합니다.");
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/api/comments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          postId: Number(post_id),
          userId: userInfo.id,
          content: newComment,
        }),
      });

      if (!res.ok) throw new Error("댓글 작성 실패");

      setNewComment("");
      fetchPost();
    } catch (err) {
      console.error(err);
    }
  };

  // 좋아요 토글
const handleLike = async () => {
  if (!userInfo) {
    alert("로그인 후 이용 가능합니다.");
    return;
  }

  try {
    const res = await fetch(
      `http://localhost:8080/api/likes/${post_id}/user/${userInfo.id}/toggle`,
      { method: "POST" }
    );
    const data = await res.json();
    setLiked(data.likedByCurrentUser);
    setLikeCount(data.likeCount);
  } catch (err) {
    console.error("좋아요 처리 실패", err);
  }
};
  const handleDelete = async () => {
  if (!userInfo) {
    alert("로그인 후 이용 가능합니다.");
    return;
  }

  if (!window.confirm("정말 삭제하시겠습니까?")) return;

  try {
    await fetch(`http://localhost:8080/api/posts/${post_id}`, {
      method: "DELETE",
    });
    alert("삭제되었습니다.");
    navigate("/"); // 삭제 후 목록 페이지로 이동
  } catch (err) {
    console.error("게시글 삭제 실패:", err);
  }
};
  if (!post) {
    return <div>로딩중...</div>;
  }

  return (
    <main className="main-content post-detail">
      <div className="post-detail-container">
        {/* 게시글 */}
        <h2 className="post-title">{post.title}</h2>
      <div className="post-meta">
  <div className="left">
    <span className="author">{post.nickname}</span>
    <span className="date">{post.createdAt.replace("T", " ")}</span>
    {userInfo?.id === post.userId && (
  <div className="post-actions">
    <button onClick={() => navigate(`/posts/${post.id}/edit`)}>✏️ 수정</button>
    <button onClick={handleDelete}>🗑 삭제</button>
  </div>
)}
  </div>
  <div className="right">
    <span className="likes">추천 {likeCount}</span>
    <span className="views">조회 12</span>  {/*{post.viewCount} */}
    <span className="comments">댓글 {post.commentCount}</span>
  </div>
</div>
        <div
            className="post-content"
            dangerouslySetInnerHTML={{ __html: post.content }}
          />
{/* 버튼은 토글 전용 */}
        <div className="post-actions">
          <button
            className={`btn-like ${liked ? "active" : ""}`}
            onClick={handleLike}
          >
            {liked ?    "좋아요취소"  :"👍좋아요 "}
          </button>
          <button className="btn-report">🚨 신고</button>
        </div>

        {/* 댓글 */}
        <div className="comment-section">
          <h3>댓글</h3>

          {post.comments && post.comments.length > 0 ? (
            post.comments.map((c) => (
              <div className="comment" key={c.id}>
                <div className="comment-author">
                  {c.userNickname}
                  <span className="comment-date">
                    {c.createdAt.replace("T", " ")}
                  </span>
                </div>
                <div className="comment-content">{c.content}</div>
              </div>
            ))
          ) : (
            <p>아직 댓글이 없습니다.</p>
          )}

          <form className="comment-form" onSubmit={handleSubmit}>
            <textarea
              placeholder={userInfo ? "댓글을 입력하세요" : "로그인 후 댓글 작성이 가능합니다."}
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              disabled={!userInfo}
            />
            
            <button type="submit" disabled={!userInfo}>
              댓글 작성
            </button>
            
          </form>
        </div>
      </div>
    </main>
  );
}
