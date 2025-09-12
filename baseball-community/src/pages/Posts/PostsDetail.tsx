import React, { useEffect, useState, useContext } from "react";
import { AuthContext } from "../../contexts/AuthContext"; // 로그인 유저 Context
import "./PostDetail.css";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

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
  viewCount: number;
  likedByCurrentUser: boolean;  // 현재 유저가 눌렀는지 여부
}

export default function PostDetail() {
  const { postId } = useParams<{ postId: string }>();
  const { userInfo } = useContext(AuthContext);
  const [post, setPost] = useState<Post | null>(null);
  const [newComment, setNewComment] = useState("");

  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);
  const navigate = useNavigate();

  // 게시글 + 댓글 불러오기
  const fetchPost = async () => {
    if (!postId) return;

    try {
      const res = await axios.get(`/api/posts/${postId}`, {
        withCredentials: true,
      });
      const data = res.data;
      setPost(data);
      setLikeCount(data.likeCount);
      setLiked(data.likedByCurrentUser);
    } catch (err) {
      console.error("게시글 불러오기 실패:", err);
    }
  };

  useEffect(() => {
    fetchPost();
  }, [postId]);

  // 댓글 작성
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim() || !postId) return;

    if (!userInfo) {
      alert("로그인 후 댓글 작성이 가능합니다.");
      return;
    }

    try {
      await axios.post(
        "/api/comments",
        {
          postId: Number(postId),
          content: newComment, //  userId 제거
        },
        { withCredentials: true }
      );

      setNewComment("");
      fetchPost();
    } catch (err) {
      console.error("댓글 작성 실패:", err);
    }
  };

  // 좋아요 토글
  const handleLike = async () => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      return;
    }

    try {
      const res = await axios.post(
        `/api/likes/${postId}/toggle`,
        {},
        { withCredentials: true }
      );
      setLiked(res.data.likedByCurrentUser);
      setLikeCount(res.data.likeCount);
    } catch (err) {
      console.error("좋아요 처리 실패", err);
    }
  };

  // 게시글 삭제
  const handleDelete = async () => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      return;
    }

    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      await axios.delete(`/api/posts/${postId}`, { withCredentials: true });
      alert("삭제되었습니다.");
      navigate("/");
    } catch (err) {
      console.error("게시글 삭제 실패:", err);
    }
  };
  // 댓글 수정
const handleEditComment = async (commentId: number, oldContent: string) => {
  const newContent = prompt("댓글 수정", oldContent);
  if (!newContent || newContent === oldContent) return;

  try {
    await axios.put(
      `/api/comments/${commentId}`,
      newContent, // @RequestBody String newContent
      {
        headers: { "Content-Type": "text/plain" },
        withCredentials: true,
      }
    );
    fetchPost();
  } catch (err) {
    console.error("댓글 수정 실패:", err);
    alert("댓글 수정에 실패했습니다.");
  }
};

// 댓글 삭제
const handleDeleteComment = async (commentId: number) => {
  if (!window.confirm("정말 이 댓글을 삭제하시겠습니까?")) return;

  try {
    await axios.delete(`/api/comments/${commentId}`, {
      withCredentials: true,
    });
    fetchPost();
  } catch (err) {
    console.error("댓글 삭제 실패:", err);
    alert("댓글 삭제에 실패했습니다.");
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
                <button onClick={() => navigate(`/posts/${post.id}/edit`)}>
                  ✏️ 수정
                </button>
                <button onClick={handleDelete}>🗑 삭제</button>
              </div>
            )}
          </div>
          <div className="right">
            <span className="likes">추천 {likeCount}</span>
            <span className="views">조회 {post.viewCount}</span>
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
            {liked ? "좋아요취소" : "👍좋아요 "}
          </button>
          <button className="btn-report">🚨 신고</button>
        </div>

        {/* 댓글 */}
        <div className="comment-section">
          <h3>댓글</h3>

       {post.comments && post.comments.length > 0 ? (
  post.comments.map((c) => (
    <div className="comment" key={c.id}>
      <div className="comment-header">
        <div className="comment-author">
          {c.userNickname}
          <span className="comment-date">
            {c.createdAt.replace("T", " ")}
          </span>
        </div>
        {/* 본인 댓글일 때만 버튼 노출 */}
        {userInfo?.nickname === c.userNickname && (
          <div className="comment-actions">
            <button
              onClick={() => handleEditComment(c.id, c.content)}
              className="btn-edit"
            >
              ✏️ 수정
            </button>
            <button
              onClick={() => handleDeleteComment(c.id)}
              className="btn-delete"
            >
              🗑 삭제
            </button>
          </div>
        )}
      </div>
      <div className="comment-content">{c.content}</div>
    </div>
  ))
) : (
  <p>아직 댓글이 없습니다.</p>
)}

          <form className="comment-form" onSubmit={handleSubmit}>
            <textarea
              placeholder={
                userInfo
                  ? "댓글을 입력하세요"
                  : "로그인 후 댓글 작성이 가능합니다."
              }
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
