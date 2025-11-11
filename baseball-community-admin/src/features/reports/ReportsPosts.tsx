import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import { mockReportsPosts } from "./mockReportsPosts";
import type { Report } from "./report";

const ReportsPosts: React.FC = () => {
  const [data] = useState<Report[]>(mockReportsPosts);

  const columns = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "게시글 ID", dataIndex: "target_id", key: "target_id" },
    { title: "신고자 ID", dataIndex: "user_id", key: "user_id" },
    {
      title: "사유",
      dataIndex: "reason",
      render: (r: string) => <Tag color="orange">{r}</Tag>,
    },
    {
      title: "상태",
      dataIndex: "status",
      render: (s: string) => (
        <Tag color={s === "pending" ? "red" : s === "reviewed" ? "blue" : "green"}>
          {s}
        </Tag>
      ),
    },
    { title: "신고일", dataIndex: "created_at" },
    {
      title: "액션",
      render: (_: any, record: Report) => (
        <Space>
          <Button type="link" onClick={() => console.log("게시글 보기", record.target_id)}>
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

export default ReportsPosts;
