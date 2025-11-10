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

      // 🔹 403 (정지된 계정)
      if (res.status === 403) {
        const data = await res.json().catch(() => ({}));
        const msg =
          data.message || "계정이 정지되었습니다. 관리자에게 문의하세요.";
        alert(msg);
        return;
      }

      // 🔹 기타 에러
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "댓글 작성 실패");
      }

      // 🔹 성공
      setContent("");
      onCommentAdded(); // 새로고침 콜백 실행
    } catch (err) {
      console.error("댓글 작성 중 오류:", err);
      alert("댓글 작성 중 오류가 발생했습니다.");
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
