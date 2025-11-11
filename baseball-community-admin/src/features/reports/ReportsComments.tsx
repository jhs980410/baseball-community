import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { mockReportsComments } from "./mockReportsComments";
import type { Report } from "../../types/report";

const ReportsComments: React.FC = () => {
  const [data] = useState<Report[]>(mockReportsComments);

  const columns: ColumnsType<Report> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "댓글 ID", dataIndex: "target_id", key: "target_id" },
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
      render: (status) => {
        const color =
          status === "pending" ? "red" : status === "reviewed" ? "blue" : "green";
        return <Tag color={color}>{status}</Tag>;
      },
    },
    { title: "신고일", dataIndex: "created_at", key: "created_at" },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => console.log("댓글 보기:", record.target_id)}>
            보기
          </Button>
          <Button type="link" danger>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  return <Table columns={columns} dataSource={data} rowKey="id" pagination={{ pageSize: 5 }} />;
};

export default ReportsComments;
