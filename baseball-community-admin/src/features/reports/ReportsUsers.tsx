import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message, Modal, Select } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import type { Report } from "./report";

const ReportsUsers: React.FC = () => {
  const [data, setData] = useState<Report[]>([]);
  const [loading, setLoading] = useState(false);
  const [actionModal, setActionModal] = useState<{ open: boolean; id?: number }>({ open: false });
  const [selectedAction, setSelectedAction] = useState<string>("");

  // 🚀 데이터 로딩
 const fetchReports = async () => {
  setLoading(true);
  try {
    const res: any = await axios.get("/api/admin/reports/users", { withCredentials: true });

    // 응답이 배열인지 확인 후 snake_case에 맞게 변환
    const list: Report[] = (Array.isArray(res.data) ? res.data : []).map((r: any) => ({
      id: r.id,
      target_type: r.target_type,
      target_id: r.target_id,
      user_id: r.user_id,
      reason: r.reason,
      status: r.status,
      created_at: r.created_at,
    }));

    setData(list);
  } catch (err) {
    console.error(err);
    message.error("사용자 신고 목록을 불러오지 못했습니다.");
  } finally {
    setLoading(false);
  }
};


  useEffect(() => {
    fetchReports();
  }, []);

  // 🎨 상태 색상
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

  // ✅ 조치 실행
  const handleAction = async () => {
    if (!actionModal.id || !selectedAction) {
      message.warning("조치 유형을 선택하세요.");
      return;
    }

    try {
      await axios.patch(
        `/api/admin/reports/${actionModal.id}/handle`,
        { action: selectedAction, adminNote: "관리자 사용자 조치" },
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
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { title: "신고된 사용자 ID", dataIndex: "targetId", key: "targetId" },
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
          <Button type="link" onClick={() => setActionModal({ open: true, id: record.id })}>
            조치
          </Button>
          <Button type="link" danger onClick={() => handleDeleteReport(record.id)}>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  // 🗑️ 신고 삭제
  const handleDeleteReport = async (id: number) => {
    try {
      await axios.delete(`/api/admin/reports/${id}`, { withCredentials: true });
      message.success("신고가 삭제되었습니다.");
      fetchReports();
    } catch (err) {
      console.error(err);
      message.error("삭제 실패");
    }
  };

  return (
    <>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        loading={loading}
      />

      {/* 🚨 조치 모달 */}
      <Modal
        title="사용자 신고 조치"
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
            { label: "경고 (WARN_USER)", value: "WARN_USER" },
            { label: "7일 정지 (SUSPEND_USER)", value: "SUSPEND_USER" },
            { label: "영구 정지 (BAN_USER)", value: "BAN_USER" },
          ]}
        />
      </Modal>
    </>
  );
};

export default ReportsUsers;
