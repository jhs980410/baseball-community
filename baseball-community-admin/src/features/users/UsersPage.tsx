import React, { useState } from "react";
import { Table, Tag, Space, Button } from "antd";
import type { ColumnsType } from "antd/es/table";
import { mockUsers } from "./mockUsers";
import { type User } from "../../types/user";  //  타입 따로 import

const UsersPage: React.FC = () => {
  const [data] = useState<User[]>(mockUsers);

  const columns: ColumnsType<User> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "이메일", dataIndex: "email", key: "email" },
    { title: "닉네임", dataIndex: "nickname", key: "nickname" },
    {
      title: "권한",
      dataIndex: "role",
      key: "role",
      render: (role: User["role"]) => {
        const color = role === "ADMIN" ? "red" : role === "MODERATOR" ? "blue" : "green";
        return <Tag color={color}>{role}</Tag>;
      },
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status: User["status"]) => {
        const color = status === "ACTIVE" ? "green" : status === "SUSPENDED" ? "orange" : "gray";
        return <Tag color={color}>{status}</Tag>;
      },
    },
    { title: "가입일", dataIndex: "created_at", key: "created_at" },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => console.log("보기:", record.id)}>보기</Button>
          <Button type="link" danger onClick={() => console.log("정지:", record.id)}>정지</Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>👤 회원 관리</h2>
      <Table<User>
        columns={columns}
        dataSource={data}
        rowKey="id"
        pagination={{ pageSize: 5 }}
      />
    </div>
  );
};

export default UsersPage;
