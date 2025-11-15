import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, Modal, Input, message, Switch } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

import type { Notice } from "../../types/notice";

const NoticesPage: React.FC = () => {
  const [data, setData] = useState<Notice[]>([]);
  const [loading, setLoading] = useState(false);

  // Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingNotice, setEditingNotice] = useState<Notice | null>(null);

  // Form
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [isPinned, setIsPinned] = useState(false);
  //날짜포맷
  const formatDate = (iso: string) => {
  if (!iso) return "";
  return iso.replace("T", " ").slice(0, 19); // yyyy-MM-dd HH:mm:ss 형태로 변환
};
  // --------------------------------------------------------
  // 📌 공지 목록 불러오기
  // --------------------------------------------------------
const fetchNotices = async () => {
  setLoading(true);
  try {
    const res: any = await axios.get("/api/admin/notices", {
      withCredentials: true
    });

    const mapped = (res.data || []).map((n: any) => ({
      id: n.id,
      title: n.title,
      content: n.content,
      is_pinned: n.is_pinned ?? n.pinned, // 둘 다 대응
      created_at: formatDate(n.created_at ?? n.createdAt),
      updated_at: formatDate(n.updated_at ?? n.updatedAt),
    }));

    setData(mapped);
  } catch (err) {
    console.error("공지 불러오기 실패:", err);
    message.error("공지 목록 불러오기 실패");
  } finally {
    setLoading(false);
  }
};


  // --------------------------------------------------------
  // 📌 새 공지 작성 Modal
  // --------------------------------------------------------
  const openCreateModal = () => {
    setEditingNotice(null);
    setTitle("");
    setContent("");
    setIsPinned(false);
    setIsModalOpen(true);
  };

  // --------------------------------------------------------
  // 📌 공지 수정 Modal
  // --------------------------------------------------------
  const openEditModal = (notice: Notice) => {
    setEditingNotice(notice);
    setTitle(notice.title);
    setContent(notice.content);
    setIsPinned(notice.is_pinned);
    setIsModalOpen(true);
  };

  // --------------------------------------------------------
  // 📌 공지 저장 (신규 + 수정)
  // --------------------------------------------------------
  const saveNotice = async () => {
    if (!title.trim()) return message.warning("제목을 입력하세요.");

    const body = {
      title,
      content,
      isPinned, // 백엔드 이름에 맞춤
    };

    try {
      if (editingNotice) {
        // 수정
        await axios.put(`/api/admin/notices/${editingNotice.id}`, body, {
          withCredentials: true,
        });
        message.success("공지 수정 완료");
      } else {
        // 생성
        await axios.post("/api/admin/notices", body, {
          withCredentials: true,
        });
        message.success("공지 등록 완료");
      }

      setIsModalOpen(false);
      fetchNotices();
    } catch {
      message.error("저장 중 오류 발생");
    }
  };

  // --------------------------------------------------------
  // 📌 공지 삭제
  // --------------------------------------------------------
  const deleteNotice = async (id: number) => {
    Modal.confirm({
      title: "공지 삭제",
      content: "정말 삭제하시겠습니까?",
      okText: "삭제",
      okType: "danger",
      cancelText: "취소",
      onOk: async () => {
        try {
          await axios.delete(`/api/admin/notices/${id}`, {
            withCredentials: true,
          });
          message.success("삭제 완료");
          fetchNotices();
        } catch {
          message.error("삭제 실패");
        }
      },
    });
  };

  // --------------------------------------------------------
  // 📌 상단 고정 토글
  // --------------------------------------------------------
  const togglePin = async (id: number) => {
    try {
      await axios.patch(`/api/admin/notices/${id}/pin`, {}, { withCredentials: true });
      fetchNotices();
    } catch {
      message.error("상단 고정 변경 실패");
    }
  };

  // --------------------------------------------------------
  // 📌 테이블 컬럼
  // --------------------------------------------------------
  const columns: ColumnsType<Notice> = [
    { title: "ID", dataIndex: "id", key: "id", width: 70 },
    { title: "제목", dataIndex: "title", key: "title" },

    {
      title: "상태",
      dataIndex: "is_pinned",
      key: "is_pinned",
      render: (v: boolean) =>
        v ? <Tag color="gold">상단 고정</Tag> : <Tag>일반</Tag>,
    },

    { title: "작성일", dataIndex: "created_at", key: "created_at" },
    { title: "수정일", dataIndex: "updated_at", key: "updated_at" },

    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => openEditModal(record)}>
            수정
          </Button>

          <Button type="link" onClick={() => togglePin(record.id)}>
            {record.is_pinned ? "고정 해제" : "상단 고정"}
          </Button>

          <Button danger type="link" onClick={() => deleteNotice(record.id)}>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  // --------------------------------------------------------
  // 📌 UI
  // --------------------------------------------------------
  return (
    <div>
      <h2>📢 공지사항 관리</h2>

      <Button type="primary" onClick={openCreateModal} style={{ marginBottom: 20 }}>
        + 새 공지 작성
      </Button>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="id"
        pagination={{ pageSize: 10 }}
      />

      {/* -------------------------------------------------- */}
      {/*              공지 작성/수정 Modal                 */}
      {/* -------------------------------------------------- */}
      <Modal
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        onOk={saveNotice}
        width={800}
        title={editingNotice ? "공지 수정" : "새 공지 작성"}
      >
        <Input
          placeholder="제목 입력"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={{ marginBottom: 12 }}
        />

        <Switch
          checked={isPinned}
          onChange={setIsPinned}
          checkedChildren="상단 고정"
          unCheckedChildren="일반"
          style={{ marginBottom: 16 }}
        />

        <ReactQuill
          value={content}
          onChange={setContent}
          theme="snow"
          style={{ height: 250, marginBottom: 20 }}
        />
      </Modal>
    </div>
  );
};

export default NoticesPage;
