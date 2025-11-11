import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import type { Report } from "./report";

const ReportsPosts: React.FC = () => {
  const [data, setData] = useState<Report[]>([]);
  const [loading, setLoading] = useState(false);

  // 🚀 신고 데이터 가져오기
  const fetchReports = async () => {
    setLoading(true);
    try {
      const res = await axios.get("/api/admin/reports/posts", { withCredentials: true });
      setData(res.data);
    } catch (err) {
      console.error(err);
      message.error("게시글 신고 목록을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  // 🎨 상태별 색상
  const statusColor = (status: string) => {
    const s = status.toLowerCase();
    switch (s) {
      case "pending": return "red";
      case "reviewed": return "blue";
      case "resolved": return "green";
      default: return "gray";
    }
  };

  // 📋 테이블 컬럼 정의
  const columns: ColumnsType<Report> = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { title: "게시글 ID", dataIndex: "targetId", key: "targetId" },
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
          <Button type="link" onClick={() => handleViewPost(record.targetId)}>
            보기
          </Button>
          <Button type="link" danger onClick={() => handleDeleteReport(record.id)}>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  // 🔍 게시글 보기 (추후 상세 페이지 연동)
  const handleViewPost = (postId: number) => {
    message.info(`(미구현) 게시글 ${postId} 보기`);
  };

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
    <Table
      columns={columns}
      dataSource={data}
      rowKey="id"
      pagination={{ pageSize: 10 }}
      loading={loading}
    />
  );
};

export default ReportsPosts;
