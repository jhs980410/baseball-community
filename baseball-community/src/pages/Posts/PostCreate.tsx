import React, { useEffect, useState, useContext } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axios from "axios";
import "./PostCreate.css";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";

export default function PostCreate() {
  const [title, setTitle] = useState<string>("");     
  const [content, setContent] = useState<string>(""); 
  const navigate = useNavigate(); 
  const { userInfo } = useContext(AuthContext);

  // 🚨 로그인 안 했으면 막기
  useEffect(() => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      navigate("/"); // 홈으로 튕기기
    }
  }, [userInfo, navigate]);

  const handleSubmit = async () => {
    if (!title.trim() || !content.trim()) {
      alert("제목과 내용을 입력해주세요.");
      return;
    }

    try {
      const payload = {
        title,
        content,
        userId: userInfo?.id, // 로그인 유저 ID
        teamId: 1,            // 팀은 선택값으로 교체 필요
      };

      await axios.post("/api/posts/insert", payload);

      alert("게시글이 등록되었습니다!");
      navigate("/");
    } catch (error) {
      console.error("게시글 등록 실패:", error);
      alert("게시글 등록 중 오류가 발생했습니다.");
    }
  };

  return (
    <main className="post-create">
      <h2>게시글 작성</h2>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="제목을 입력하세요"
        style={{ width: "100%", padding: "10px", marginBottom: "20px", fontSize: "18px" }}
      />
      <ReactQuill
        theme="snow"
        value={content}
        onChange={(value: string) => setContent(value)}
        placeholder="내용을 입력하세요..."
        style={{ height: "300px", marginBottom: "50px" }}
      />
      <button
        onClick={handleSubmit}
        style={{
          padding: "10px 20px",
          fontSize: "16px",
          backgroundColor: "#1976d2",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
      >
        등록
      </button>
    </main>
  );
}
