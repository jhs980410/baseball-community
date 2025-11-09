import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import { type User } from "../../types/user"; // User 타입은 그대로 유지

const UsersPage: React.FC = () => {
  const [data, setData] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  //  백엔드 연동
  useEffect(() => {
    setLoading(true);
    axios
      .get("/api/admin/users", { withCredentials: true }) // JWT 쿠키 기반 인증이라면
      .then((res) => {
        // Spring의 Page 응답이면 content 필드 안에 데이터 있음
        const users = res.data.content || res.data;
        setData(users);
      })
      .catch((err) => {
        console.error("회원 목록 불러오기 실패:", err);
        message.error("회원 목록을 불러오지 못했습니다.");
      })
      .finally(() => setLoading(false));
  }, []);

  const columns: ColumnsType<User> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "이메일", dataIndex: "email", key: "email" },
    { title: "닉네임", dataIndex: "nickname", key: "nickname" },
    {
      title: "권한",
      dataIndex: "role",
      key: "role",
      render: (role: User["role"]) => {
        const color =
          role === "ADMIN" ? "red" : role === "MODERATOR" ? "blue" : "green";
        return <Tag color={color}>{role}</Tag>;
      },
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status: User["status"]) => {
        const color =
          status === "ACTIVE"
            ? "green"
            : status === "SUSPENDED"
            ? "orange"
            : "gray";
        return <Tag color={color}>{status}</Tag>;
      },
    },
    {
      title: "가입일",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (date: string) =>
        date ? new Date(date).toLocaleString() : "—",
    },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleView(record.id)}>
            보기
          </Button>
          <Button type="link" danger onClick={() => handleSuspend(record.id)}>
            정지
          </Button>
        </Space>
      ),
    },
  ];

  const handleView = (id: number) => {
    console.log("보기:", id);
    // TODO: 모달로 상세조회 or 페이지 이동
  };

  const handleSuspend = (id: number) => {
    axios
      .patch(`/api/admin/users/${id}/status`, { status: "SUSPENDED" })
      .then(() => {
        message.success(`사용자 #${id} 정지 처리 완료`);
        setData((prev) =>
          prev.map((u) =>
            u.id === id ? { ...u, status: "SUSPENDED" } : u
          )
        );
      })
      .catch(() => message.error("정지 처리 실패"));
  };

  return (
    <div>
      <h2>👤 회원 관리</h2>
      <Table<User>
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 10 }}
      />
    </div>
  );
};

export default UsersPage;
