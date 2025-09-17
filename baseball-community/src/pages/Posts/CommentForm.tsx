import React, { useState } from "react";

interface CommentFormProps {
  postId: number;
  parentId?: number | null; // 대댓글일 경우 부모 댓글 ID
  onCommentAdded: () => void;
}

export default function CommentForm({
  postId,
  parentId = null,
  onCommentAdded,
}: CommentFormProps) {
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
        credentials: "include", // JWT 쿠키 인증
        body: JSON.stringify({
          postId,
          content,
          parentId,
        }),
      });

      if (!res.ok) {
        throw new Error("댓글 작성 실패");
      }

      setContent("");
      onCommentAdded(); // 새로고침 콜백 실행
    } catch (err) {
      console.error(err);
      alert("댓글 작성 실패");
    }
  };

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <textarea
        placeholder={parentId ? "답글을 입력하세요" : "댓글을 입력하세요"}
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <button type="submit">{parentId ? "답글 작성" : "댓글 작성"}</button>
    </form>
  );
}
