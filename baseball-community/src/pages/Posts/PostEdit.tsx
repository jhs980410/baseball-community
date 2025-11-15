import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../../contexts/AuthContext";
import { teams } from "../../constants/teams";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import "./PostEdit.css";

export default function PostEdit() {
const { postId } = useParams<{ postId: string }>();
  const [title, setTitle] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const [teamId, setTeamId] = useState<string>("1");
  const navigate = useNavigate();
  const { userInfo } = useContext(AuthContext);

  // 기존 게시글 불러오기
  useEffect(() => {
    if (!userInfo) {
      alert("로그인 후 이용 가능합니다.");
      navigate("/");
      return;
    }
console.log(postId+"이거널?");
   axios.get(`/api/posts/${postId}`).then((res: any) => {
  setTitle(res.data.title);
  setContent(res.data.content);
  setTeamId(String(res.data.teamId));
});
  }, [userInfo, navigate, postId]);

  const handleSubmit = async () => {
  if (!title.trim() || !content.trim()) {
    alert("제목과 내용을 입력해주세요.");
    return;
  }

  try {
    const payload = {
      title,
      content,
      teamId: Number(teamId), // ✅ userId 제거
    };

    await axios.put(`/api/posts/${postId}`, payload, { withCredentials: true });

    alert("게시글이 수정되었습니다!");
    navigate("/");
  } catch (error) {
    console.error("게시글 수정 실패:", error);
    alert("게시글 수정 중 오류가 발생했습니다.");
  }
};


  return (
    <main className="post-edit">
      <h2 className="form-title">게시글 수정</h2>

      {/* 팀 선택 라디오 */}
      <div className="team-select">
        <strong>팀 선택:</strong>
        <div className="radio-group">
          {teams
            .filter((t) => t.id !== "all")
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
        className="title-input"
      />

      {/* 본문 입력 */}
      <ReactQuill
        theme="snow"
        value={content}
        onChange={(value: string) => setContent(value)}
        placeholder="내용을 입력하세요..."
        className="editor"
      />

      {/* 수정 버튼 */}
      <button onClick={handleSubmit} className="submit-btn">
        수정 완료
      </button>
    </main>
  );
}
