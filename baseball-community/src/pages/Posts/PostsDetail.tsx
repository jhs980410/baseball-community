import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext"; // 로그인 유저 Context
import "./PostDetail.css";

interface Comment {
  id: number;
  content: string;
  userNickname: string;
  createdAt: string;
}

interface Post {
  id: number;
  title: string;
  content: string;
  nickname: string;
  createdAt: string;
  comments: Comment[]; // 백엔드에서 댓글 포함해서 내려주는 경우
}

export default function PostDetail() {
  const { post_id } = useParams<{ post_id: string }>();
  const { userInfo } = useContext(AuthContext); //  로그인 유저 정보
  const [post, setPost] = useState<Post | null>(null);
  const [newComment, setNewComment] = useState("");

  // 게시글 + 댓글 불러오기
  const fetchPost = () => {
    if (!post_id) return;
    fetch(`http://localhost:8080/api/posts/${post_id}`)
      .then((res) => res.json())
      .then((data) => setPost(data))
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
          userId: userInfo.id, //  로그인 유저 id
          content: newComment,
        }),
      });

      if (!res.ok) throw new Error("댓글 작성 실패");

      setNewComment(""); // 입력창 비우기
      fetchPost(); // 댓글 목록 새로고침
    } catch (err) {
      console.error(err);
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
          <span>작성자: {post.nickname}</span>
          <span>{post.createdAt.replace("T", " ")}</span>
        </div>
        <div className="post-content">{post.content}</div>

        {/* 댓글 */}
        <div className="comment-section">
          <h3>댓글</h3>

          {/* 댓글 목록 */}
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

          {/* 댓글 작성 */}
          <form className="comment-form" onSubmit={handleSubmit}>
            <textarea
              placeholder={userInfo ? "댓글을 입력하세요" : "로그인 후 댓글 작성이 가능합니다."}
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              disabled={!userInfo} // 로그인 안 하면 입력 막기
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
