import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { mockReports } from "./mockReports";
import type { Report } from "../../types/report";

const ReportsPage: React.FC = () => {
  const [data] = useState<Report[]>(mockReports);

  const columns: ColumnsType<Report> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "대상 타입", dataIndex: "target_type", key: "target_type" },
    { title: "대상 ID", dataIndex: "target_id", key: "target_id" },
    { title: "신고자 ID", dataIndex: "user_id", key: "user_id" },
    {
      title: "사유",
      dataIndex: "reason",
      key: "reason",
      render: (reason: Report["reason"]) => <Tag color="orange">{reason}</Tag>,
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status: Report["status"]) => {
        const color =
          status === "pending" ? "red" : status === "reviewed" ? "blue" : "green";
        return <Tag color={color}>{status}</Tag>;
      },
    },
    { title: "신고일", dataIndex: "created_at", key: "created_at" },
    {
      title: "액션",
      key: "action",
     render: (_, record) => {
  return (
    <Space>
      <Button type="link" onClick={() => console.log("보기:", record.id)}>
        보기
      </Button>
      <Button type="link" danger onClick={() => console.log("삭제:", record.id)}>
        삭제
      </Button>
    </Space>
  );
},

    },
  ];

  return (
    <div>
      <h2>🚨 신고 관리</h2>
      <Table<Report>
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={{ pageSize: 5 }}
      />
    </div>
  );
};

export default ReportsPage;
