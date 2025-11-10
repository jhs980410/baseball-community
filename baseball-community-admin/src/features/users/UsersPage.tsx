import React, { useEffect, useState } from "react";
import {
  Table,
  Tag,
  Space,
  Button,
  message,
  Modal,
  Descriptions,
  Input,
  Select,
} from "antd";
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

  /** 🔹 상세 조회 */
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

  /** 🔹 회원 정지 (사유+기간 선택) */
  const handleSuspend = (id: number) => {
    let reason = "";
    let duration = 24; // 기본 24시간

    Modal.confirm({
      title: "⚠️ 회원 정지 설정",
      width: 450,
      content: (
        <div>
          <p>정지 사유를 입력하세요:</p>
          <Input
            placeholder="예: 비속어 사용, 도배 행위 등"
            onChange={(e) => (reason = e.target.value)}
          />
          <p style={{ marginTop: 10 }}>정지 기간 선택:</p>
          <Select
            defaultValue="24"
            onChange={(v) => (duration = Number(v))}
            style={{ width: "100%" }}
          >
            <Select.Option value="1">1시간</Select.Option>
            <Select.Option value="6">6시간</Select.Option>
            <Select.Option value="24">1일</Select.Option>
            <Select.Option value="72">3일</Select.Option>
            <Select.Option value="168">7일</Select.Option>
            <Select.Option value="0">영구정지</Select.Option>
          </Select>
        </div>
      ),
      okText: "정지 적용",
      cancelText: "취소",
      async onOk() {
        try {
          await axios.patch(
            `/api/admin/users/${id}/suspend`,
            { reason, durationHours: duration },
            { withCredentials: true }
          );
          message.success("회원이 정지되었습니다.");
          fetchUsers();
        } catch (err) {
          console.error("정지 실패:", err);
          message.error("회원 정지 처리에 실패했습니다.");
        }
      },
    });
  };

  /** 🔹 복구 */
  const handleUnsuspend = async (id: number) => {
    try {
      await axios.patch(`/api/admin/users/${id}/unsuspend`, null, {
        withCredentials: true,
      });
      message.success("회원 정지가 해제되었습니다.");
      fetchUsers();
    } catch (err) {
      console.error("복구 실패:", err);
      message.error("회원 복구에 실패했습니다.");
    }
  };

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

          {record.status === "ACTIVE" ? (
            <Button type="link" danger onClick={() => handleSuspend(record.id)}>
              정지
            </Button>
          ) : (
            <Button type="link" onClick={() => handleUnsuspend(record.id)}>
              복구
            </Button>
          )}
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
