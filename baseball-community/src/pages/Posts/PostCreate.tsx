import React, { useEffect, useState, useContext } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axios from "axios";
import "./PostCreate.css";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";
import { teams } from "../../constants/teams";

export default function PostCreate() {
  const [title, setTitle] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const [teamId, setTeamId] = useState<string>("1"); // 기본값 두산
  const navigate = useNavigate();
  const { userInfo } = useContext(AuthContext);

  // 로그인 안 했으면 막기
  useEffect(() => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      navigate("/"); 
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
        userId: userInfo?.id,
        teamId: Number(teamId), // 선택한 팀 ID
      };

      await axios.post("/api/posts/insert", payload);

      alert("게시글이 등록되었습니다!");
      navigate(`/team/${teamId}`); // 선택한 팀 게시판으로 이동
    } catch (error) {
      console.error("게시글 등록 실패:", error);
      alert("게시글 등록 중 오류가 발생했습니다.");
    }
  };

  return (
    <main className="post-create">
      <h2>게시글 작성</h2>

      {/* 팀 선택 라디오 */}
      <div style={{ marginBottom: "20px" }}>
        <strong>팀 선택:</strong>
        <div style={{ display: "flex", flexWrap: "wrap", gap: "15px", marginTop: "10px" }}>
          {teams
            .filter((t) => t.id !== "all") // 전체 제외
            .map((team) => (
              <label key={team.id}>
                <input
                  type="radio"
                  name="team"
                  value={team.id}
                  checked={teamId === team.id}
                  onChange={() => setTeamId(team.id)}
                />
                {team.name}
              </label>
            ))}
        </div>
      </div>

      {/* 제목 입력 */}
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="제목을 입력하세요"
        style={{
          width: "100%",
          padding: "10px",
          marginBottom: "20px",
          fontSize: "18px",
        }}
      />

      {/* 본문 입력 */}
      <ReactQuill
        theme="snow"
        value={content}
        onChange={(value: string) => setContent(value)}
        placeholder="내용을 입력하세요..."
        style={{ height: "300px", marginBottom: "50px" }}
      />

      {/* 등록 버튼 */}
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
