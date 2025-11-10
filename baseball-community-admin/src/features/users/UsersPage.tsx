import React, { useEffect, useState } from "react";
import { Table, Tag, Space, Button, message, Modal, Descriptions } from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";
import { type User } from "../../types/user";

interface UserDetail extends User {
  reportCount: number;
  postCount: number;
  commentCount: number;
}

const UsersPage: React.FC = () => {
  const [data, setData] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  //  유저 목록 불러오기
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await axios.get("/api/admin/users", { withCredentials: true });
      const users = res.data.content || res.data;
      setData(users);
    } catch (err) {
      console.error("회원 목록 불러오기 실패:", err);
      message.error("회원 목록을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  //  상세 조회
  const handleView = async (id: number) => {
    try {
      const res = await axios.get(`/api/admin/users/${id}`, { withCredentials: true });
      const user = res.data as UserDetail;

      Modal.info({
        title: `👤 사용자 #${id} 상세 정보`,
        width: 550,
        content: (
          <Descriptions bordered column={1} size="small">
            <Descriptions.Item label="이메일">{user.email}</Descriptions.Item>
            <Descriptions.Item label="닉네임">{user.nickname}</Descriptions.Item>
            <Descriptions.Item label="권한">
              <Tag
                color={
                  user.role === "ADMIN"
                    ? "red"
                    : user.role === "MODERATOR"
                    ? "blue"
                    : "green"
                }
              >
                {user.role}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="상태">
              <Tag
                color={
                  user.status === "ACTIVE"
                    ? "green"
                    : user.status === "SUSPENDED"
                    ? "orange"
                    : "gray"
                }
              >
                {user.status}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="신고 횟수">
              <Tag
                color={
                  user.reportCount >= 3
                    ? "red"
                    : user.reportCount > 0
                    ? "orange"
                    : "default"
                }
              >
                {user.reportCount}회
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="작성 글 수">{user.postCount}개</Descriptions.Item>
            <Descriptions.Item label="작성 댓글 수">{user.commentCount}개</Descriptions.Item>
            <Descriptions.Item label="가입일">
              {new Date(user.createdAt).toLocaleString()}
            </Descriptions.Item>
          </Descriptions>
        ),
      });
    } catch (err) {
      console.error("상세 조회 실패:", err);
      message.error("회원 상세 정보를 불러오지 못했습니다.");
    }
  };

  //  상태 변경 (정지 ↔ 복구)
  const handleToggleStatus = async (id: number, currentStatus: string) => {
    const newStatus = currentStatus === "ACTIVE" ? "SUSPENDED" : "ACTIVE";
    try {
      await axios.patch(
        `/api/admin/users/${id}`,
        { status: newStatus },
        { withCredentials: true }
      );
      message.success(`사용자 #${id} 상태가 ${newStatus === "SUSPENDED" ? "정지" : "복구"}되었습니다.`);
      setData((prev) =>
        prev.map((u) => (u.id === id ? { ...u, status: newStatus } : u))
      );
    } catch (err) {
      console.error("상태 변경 실패:", err);
      message.error("상태 변경에 실패했습니다.");
    }
  };

  //  테이블 컬럼
  const columns: ColumnsType<User> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "이메일", dataIndex: "email", key: "email" },
    { title: "닉네임", dataIndex: "nickname", key: "nickname" },
    {
      title: "권한",
      dataIndex: "role",
      key: "role",
      render: (role: string) => (
        <Tag color={role === "ADMIN" ? "red" : role === "MODERATOR" ? "blue" : "green"}>
          {role}
        </Tag>
      ),
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status: string) => (
        <Tag color={status === "ACTIVE" ? "green" : status === "SUSPENDED" ? "orange" : "gray"}>
          {status}
        </Tag>
      ),
    },
    {
      title: "가입일",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (date: string) => (date ? new Date(date).toLocaleString() : "—"),
    },
    {
      title: "액션",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => handleView(record.id)}>
            보기
          </Button>
          <Button
            type="link"
            danger={record.status === "ACTIVE"}
            onClick={() => handleToggleStatus(record.id, record.status)}
          >
            {record.status === "ACTIVE" ? "정지" : "복구"}
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>👥 회원 관리</h2>
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
