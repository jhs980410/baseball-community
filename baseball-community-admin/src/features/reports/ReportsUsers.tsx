import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import type { Report } from "./report";

const ReportsUsers: React.FC = () => {
  const [data, setData] = useState<Report[]>([]);
  const [loading, setLoading] = useState(false);

  //  데이터 로딩
  const fetchReports = async () => {
    setLoading(true);
    try {
      const res = await axios.get("/api/admin/reports/users", { withCredentials: true });
      setData(res.data);
    } catch (err) {
      console.error(err);
      message.error("신고 목록을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  //  신고 상태 색상 지정
  const statusColor = (status: string) => {
    switch (status) {
      case "pending": return "red";
      case "reviewed": return "blue";
      case "resolved": return "green";
      default: return "gray";
    }
  };

  //  테이블 컬럼 정의
  const columns: ColumnsType<Report> = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { title: "신고된 사용자 ID", dataIndex: "target_id", key: "target_id" },
    { title: "신고자 ID", dataIndex: "user_id", key: "user_id" },
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
    { title: "신고일", dataIndex: "created_at", key: "created_at" },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            onClick={() => handleSuspendUser(record.target_id)}
          >
            정지
          </Button>
          <Button
            type="link"
            danger
            onClick={() => handleDeleteReport(record.id)}
          >
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  //  신고 삭제
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

  //  사용자 정지 (추후 /api/admin/users/suspend 로 연결)
  const handleSuspendUser = (userId: number) => {
    message.info(`(미구현) 사용자 ${userId} 정지 기능`);
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

export default ReportsUsers;
