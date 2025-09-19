import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { mockPosts } from "./mockPosts";
import type { Post } from "../../types/post";

const PostsPage: React.FC = () => {
  const [data] = useState<Post[]>(mockPosts);

  const columns: ColumnsType<Post> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "작성자 ID", dataIndex: "user_id", key: "user_id" },
    { title: "팀", dataIndex: "team_id", key: "team_id" },
    { title: "제목", dataIndex: "title", key: "title" },
    {
      title: "상태",
      dataIndex: "is_hidden",
      key: "is_hidden",
      render: (is_hidden: boolean) =>
        is_hidden ? <Tag color="red">숨김</Tag> : <Tag color="green">노출</Tag>,
    },
    { title: "작성일", dataIndex: "created_at", key: "created_at" },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => console.log("보기:", record.id)}>
            보기
          </Button>
          <Button type="link" danger>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>📝 게시글 관리</h2>
      <Table<Post>
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={{ pageSize: 5 }}
      />
    </div>
  );
};

export default PostsPage;
