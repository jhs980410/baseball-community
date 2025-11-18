import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./notice.css";

interface Notice {
  id: number;
  title: string;
  content: string;
  isPinned: boolean;
  createdAt: string;
  updatedAt: string;
}

export default function NoticeDetail() {
  const { id } = useParams<{ id: string }>();
  const [notice, setNotice] = useState<Notice | null>(null);
  const navigate = useNavigate();

  const fetchNotice = async () => {
    try {
      const res: any = await axios.get(`/api/notices/${id}`, { withCredentials: true });
      setNotice(res.data);
    } catch (err) {
      console.error("공지 불러오기 실패:", err);
    }
  };

  useEffect(() => {
    fetchNotice();
  }, [id]);

  if (!notice) return <div>로딩중...</div>;

  return (
    <main className="notice-detail-container">
      <div className="notice-header">
        {notice.isPinned && <div className="notice-pin">📌 상단 고정</div>}
        <h2 className="notice-title">{notice.title}</h2>

        <div className="notice-meta">
          <span className="date">작성일 {notice.createdAt.replace("T", " ")}</span>
          {notice.updatedAt !== notice.createdAt && (
            <span className="date">(수정됨)</span>
          )}
        </div>
      </div>

      <div
        className="notice-content"
        dangerouslySetInnerHTML={{ __html: notice.content }}
      />

      <div className="back-to-list">
        <button onClick={() => navigate(-1)}>목록으로 가기</button>
      </div>
    </main>
  );
}
