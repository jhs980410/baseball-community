import React, { useState } from "react";

interface CommentFormProps {
  postId: number;
  userId: number; // 로그인 유저 ID (AuthContext에서 가져올 수도 있음)
  onCommentAdded: () => void; // 댓글 작성 후 목록 새로고침용
}

export default function CommentForm({ postId, userId, onCommentAdded }: CommentFormProps) {
  const [content, setContent] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!content.trim()) return;

    try {
      const res = await fetch("http://localhost:8080/api/comments", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          postId,
          userId,
          content,
        }),
      });

      if (!res.ok) {
        throw new Error("댓글 작성 실패");
      }

      setContent(""); // 입력창 비우기
      onCommentAdded(); // 댓글 목록 새로고침
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <textarea
        placeholder="댓글을 입력하세요"
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <button type="submit">댓글 작성</button>
    </form>
  );
}
