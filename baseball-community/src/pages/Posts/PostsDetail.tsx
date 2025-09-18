import React, { useEffect, useState, useContext } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import "./PostDetail.css";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import CommentForm from "./CommentForm";

// Comment 타입 
interface Comment {
  id: number;
  userId: number | null;
  userNickname: string;
  content: string;
  createdAt: string;       //  date → createdAt
  parentId: number | null;
  children: Comment[];
  likeCount: number;       //  추가
  likedByCurrentUser: boolean; //  추가
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
  likeCount: number;
  viewCount: number;
  likedByCurrentUser: boolean;
}

export default function PostDetail() {
  const { postId } = useParams<{ postId: string }>();
  const { userInfo } = useContext(AuthContext);
  const [post, setPost] = useState<Post | null>(null);
  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);
  const [replyParentId, setReplyParentId] = useState<number | null>(null);
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


  
const handleDeletePosts = async (postId: number) => {
  if (!window.confirm("정말 이 게시글을 삭제하시겠습니까?")) return;
  try {
    const response = await axios.delete(`/api/posts/${postId}`, {
      withCredentials: true,
    });
    
    console.log("삭제 성공:", response);
    
    // 타임아웃 추가해서 비동기 처리 순서 보장
    setTimeout(() => {
      navigate('/');
    }, 100);
    
  } catch (err) {
    console.error("게시글 삭제 실패:", err);
    alert("게시글 삭제에 실패했습니다. 다시 시도해주세요.");
  }
};

  useEffect(() => {
    fetchPost();
  }, [postId]);

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

  // 댓글 수정
  const handleEditComment = async (commentId: number, oldContent: string) => {
    const newContent = prompt("댓글 수정", oldContent);
    if (!newContent || newContent === oldContent) return;

    try {
      await axios.put(`/api/comments/${commentId}`, newContent, {
        headers: { "Content-Type": "text/plain" },
        withCredentials: true,
      });
      fetchPost();
    } catch (err) {
      console.error("댓글 수정 실패:", err);
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
    }
  };

  if (!post) {
    return <div>로딩중...</div>;
  }

  // 재귀적으로 댓글 + 대댓글 렌더링
const renderComments = (comments: Comment[], depth = 0) =>
  comments.map((c) => (
    <div className="comment" key={c.id} style={{ marginLeft: depth * 20 }}>
      <div className="comment-header">
        <div className="comment-author">
          {c.userNickname}
          <span className="comment-date">{c.createdAt.replace("T", " ")}</span>
        </div>
        {userInfo?.nickname === c.userNickname && (
          <div className="comment-actions">
            <button onClick={() => handleEditComment(c.id, c.content)} className="btn-edit">✏️ 수정</button>
            <button onClick={() => handleDeleteComment(c.id)} className="btn-delete">🗑 삭제</button>
          </div>
        )}
      </div>

      <div className="comment-content">{c.content}</div>

   <div className="comment-actions-bar">
        <button onClick={() => setReplyParentId(c.id)}>💬 답글</button>
        <button onClick={() => handleLikeComment(c.id, c.likedByCurrentUser ?? false)}>
          👍 추천 {c.likeCount ?? 0}
        </button>
        <button onClick={() => handleReportComment(c.id)}>🚨 신고</button>
      </div>
      {replyParentId === c.id && (
        <CommentForm
          postId={post.id}
          parentId={c.id}
          onCommentAdded={() => {
            fetchPost();
            setReplyParentId(null);
          }}
        />
      )}

      {/* children 재귀 */}
      {c.children && c.children.length > 0 && renderComments(c.children, depth + 1)}
    </div>
  ));

// 댓글 좋아요 (부분 동기화)
const handleLikeComment = async (commentId: number, likedByCurrentUser: boolean) => {
  try {
    let res;

    if (likedByCurrentUser) {
      // 이미 좋아요 누른 상태 → DELETE
      res = await axios.delete(`/api/likes/comments/${commentId}`, {
        withCredentials: true,
      });
      console.log("좋아요 취소됨:", res.data);
    } else {
      // 아직 안 누른 상태 → POST
      res = await axios.post(
        `/api/likes/comments/${commentId}`,
        {},
        { withCredentials: true }
      );
      console.log("좋아요 추가됨:", res.data);
    }

    // 서버 응답 DTO 구조: { commentId, likeCount, dislikeCount, likedByCurrentUser, dislikedByCurrentUser }
    const updated = res.data;

    // 상태 갱신 (post.comments에서 해당 comment만 업데이트)
    setPost((prev) => {
      if (!prev) return prev;

      const updateComments = (comments: Comment[]): Comment[] =>
        comments.map((c) => {
          if (c.id === updated.commentId) {
            return {
              ...c,
              likeCount: updated.likeCount,
              likedByCurrentUser: updated.likedByCurrentUser,
            };
          }
          return {
            ...c,
            children: updateComments(c.children), // 재귀적으로 대댓글도 갱신
          };
        });

      return {
        ...prev,
        comments: updateComments(prev.comments),
      };
    });
  } catch (err) {
    console.error("댓글 좋아요 실패:", err);
  }
};

// 게시글 신고
const handleReportPosts = async (postId: number) => {
  const reason = prompt("신고 사유를 선택/입력하세요 (SPAM/ADULT/PERSONAL_INFO/ABUSE)", "SPAM");
  if (!reason) return;

  try {
    await axios.post(
      `/api/reports/posts/${postId}`,
      { reason },
      { withCredentials: true }
    );
    alert("신고가 접수되었습니다.");
  } catch (err) {
    console.error("댓글 신고 실패:", err);
  }
};


// 댓글 신고
const handleReportComment = async (commentId: number) => {
  const reason = prompt("신고 사유를 선택/입력하세요 (SPAM/ADULT/PERSONAL_INFO/ABUSE)", "SPAM");
  if (!reason) return;

  try {
    await axios.post(
      `/api/reports/comments/${commentId}`,
      { reason },
      { withCredentials: true }
    );
    alert("신고가 접수되었습니다.");
  } catch (err) {
    console.error("댓글 신고 실패:", err);
  }
};
  return (
    <main className="main-content post-detail">
      <div className="post-detail-container">
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
                <button onClick={() => handleDeletePosts(post.id)}>🗑 삭제</button>
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

        <div className="post-actions">
          <button
            className={`btn-like ${liked ? "active" : ""}`}
            onClick={handleLike}
          >
            {liked ? "좋아요취소" : "👍좋아요 "}
          </button>
          <button className="btn-report" onClick={() => handleReportPosts(post.id)} >🚨 신고</button>
        </div>

     
                  {/* 댓글 영역 */}
          <div className="comment-section">
            <h3>댓글</h3>
            {post.comments && post.comments.length > 0 ? (
              // parentId == null 인 것만 루트로 렌더링
              renderComments(post.comments.filter(c => c.parentId === null))
            ) : (
              <p>아직 댓글이 없습니다.</p>
            )}

            {/* 최상위 댓글 입력폼 */}
            <CommentForm
              postId={post.id}
              parentId={null}
              onCommentAdded={fetchPost}
            />
        </div>
      </div>
    </main>
  );
}
