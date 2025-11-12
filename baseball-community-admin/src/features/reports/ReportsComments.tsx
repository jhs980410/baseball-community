import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message, Modal, Select } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import type { Report } from "../../types/report";

const ReportsComments: React.FC = () => {
  const [data, setData] = useState<Report[]>([]);
  const [loading, setLoading] = useState(false);
  const [actionModal, setActionModal] = useState<{ open: boolean; id?: number }>({ open: false });
  const [selectedAction, setSelectedAction] = useState<string>("");

  // 🚀 댓글 신고 목록 조회
  const fetchReports = async () => {
    setLoading(true);
    try {
      const res = await axios.get("/api/admin/reports/comments", { withCredentials: true });
      setData(res.data);
    } catch (err) {
      console.error(err);
      message.error("댓글 신고 목록을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  // 🎨 상태 컬러
  const statusColor = (status: string) => {
    const s = status.toLowerCase();
    switch (s) {
      case "pending":
        return "red";
      case "reviewed":
        return "blue";
      case "resolved":
        return "green";
      default:
        return "gray";
    }
  };

  // ✅ 조치 요청 (PATCH)
  const handleAction = async () => {
    if (!actionModal.id || !selectedAction) {
      message.warning("조치 내용을 선택하세요.");
      return;
    }

    try {
      await axios.patch(
        `/api/admin/reports/${actionModal.id}/handle`,
        { action: selectedAction, adminNote: "관리자 조치" },
        { withCredentials: true }
      );
      message.success("조치가 완료되었습니다.");
      setActionModal({ open: false });
      setSelectedAction("");
      fetchReports();
    } catch (err) {
      console.error(err);
      message.error("조치 처리 중 오류가 발생했습니다.");
    }
  };

  // 📋 테이블 컬럼 정의
  const columns: ColumnsType<Report> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "댓글 ID", dataIndex: "targetId", key: "targetId" },
    { title: "신고자 ID", dataIndex: "reporterId", key: "reporterId" },
    {
      title: "사유",
      dataIndex: "reason",
      key: "reason",
      render: (reason) => <Tag color="orange">{reason}</Tag>,
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status) => <Tag color={statusColor(status)}>{status}</Tag>,
    },
    { title: "신고일", dataIndex: "createdAt", key: "createdAt" },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleViewComment(record.targetId)}>
            보기
          </Button>
          <Button type="link" onClick={() => setActionModal({ open: true, id: record.id })}>
            조치
          </Button>
        </Space>
      ),
    },
  ];

  // 🔍 댓글 보기 (미구현)
  const handleViewComment = (commentId: number) => {
    message.info(`(미구현) 댓글 ${commentId} 보기`);
  };

  return (
    <>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 5 }}
      />

      {/* 조치 모달 */}
      <Modal
        title="댓글 신고 조치"
        open={actionModal.open}
        onOk={handleAction}
        onCancel={() => setActionModal({ open: false })}
        okText="확인"
        cancelText="취소"
      >
        <p>조치 유형을 선택하세요:</p>
        <Select
          style={{ width: "100%" }}
          placeholder="조치 선택"
          onChange={setSelectedAction}
          value={selectedAction}
          options={[
            { label: "댓글 숨김", value: "HIDE_COMMENT" },
            { label: "댓글 삭제", value: "DELETE_COMMENT" },
            { label: "작성자 경고", value: "WARN_USER" },
          ]}
        />
      </Modal>
    </>
  );
};

export default ReportsComments;
