import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../../contexts/AuthContext";

export default function PostEdit() {
  const { postId } = useParams();
  const navigate = useNavigate();
  const { userInfo } = useContext(AuthContext);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  // 기존 데이터 불러오기
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const res = await axios.get(`/api/posts/${postId}`);
        setTitle(res.data.title);
        setContent(res.data.content);
      } catch (err) {
        console.error("게시글 불러오기 실패:", err);
      }
    };
    fetchPost();
  }, [postId]);

  // 수정 요청
  const handleUpdate = async () => {
    try {
      await axios.put(`/api/posts/${postId}`, {
        title,
        content,
        userId: userInfo.id, // 인증된 사용자
      });
      alert("게시글이 수정되었습니다.");
      navigate(`/posts/${postId}`); // 수정 후 상세 페이지로 이동
    } catch (err) {
      console.error("수정 실패:", err);
    }
  };

  return (
    <div className="post-edit">
      <h2>게시글 수정</h2>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="제목을 입력하세요"
      />
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="내용을 입력하세요"
      />
      <button onClick={handleUpdate}>수정 완료</button>
    </div>
  );
}
