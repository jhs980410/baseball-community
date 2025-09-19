import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { mockNotices } from "./mockNotices";
import type { Notice } from "../../types/notice";


const NoticesPage: React.FC = () => {
  const [data] = useState<Notice[]>(mockNotices);

  const columns: ColumnsType<Notice> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "제목", dataIndex: "title", key: "title" },
    {
      title: "상태",
      dataIndex: "is_pinned",
      key: "is_pinned",
      render: (is_pinned: boolean) =>
        is_pinned ? <Tag color="gold">상단 고정</Tag> : <Tag color="default">일반</Tag>,
    },
    { title: "작성일", dataIndex: "created_at", key: "created_at" },
    { title: "수정일", dataIndex: "updated_at", key: "updated_at" },
    {
      title: "액션",
      key: "action",
                render: (_, record) => (
            <Space>
                <Button type="link" onClick={() => console.log("보기:", record.id)}>
                보기
                </Button>
                <Button type="link" onClick={() => console.log("수정:", record.id)}>
                수정
                </Button>
                <Button type="link" danger onClick={() => console.log("삭제:", record.id)}>
                삭제
                </Button>
            </Space>
            ),

    },
  ];

  return (
    <div>
      <h2>📢 공지사항 관리</h2>
      <Table<Notice>
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={{ pageSize: 5 }}
      />
    </div>
  );
};

export default NoticesPage;
