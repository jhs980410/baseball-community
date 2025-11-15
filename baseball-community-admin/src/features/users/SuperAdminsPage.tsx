import React, { useEffect, useState } from "react";
import {
  Table,
  Tag,
  Space,
  Button,
  message,
  Modal,
  Descriptions,
} from "antd";
import type { ColumnsType } from "antd/es/table";
import axios from "axios";

/** 🔹 AdminUser 타입 (snake_case 기반 자동 변환 지원) */
interface AdminUser {
  id: number;
  email: string;
  nickname: string;
  role: "USER" | "ADMIN" | "SUPER_ADMIN";
  status: "ACTIVE" | "SUSPENDED" | "DELETED";
  createdAt: string; // camelCase로 변환하여 저장
}

const SuperAdminsPage: React.FC = () => {
  const [data, setData] = useState<AdminUser[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  /** 🔹 관리자 목록 가져오기 */
const fetchAdmins = async () => {
  setLoading(true);
  try {
    const res = await axios.get("/api/super/admins", {
      withCredentials: true,
    });

    // 🔥 1) 응답에서 content가 배열인지 체크
    const raw = Array.isArray((res.data as any)?.content)
      ? (res.data as any).content
      : (res.data as any);

    // 🔥 2) content 또는 배열 형태 모두 처리
    const list = raw.map((a: any) => ({
      id: a.id,
      email: a.email,
      nickname: a.nickname,
      role: a.role,
      status: a.status,
      createdAt: a.created_at, // snake_case → camelCase
    }));

    setData(list);
  } catch (err) {
    console.error("관리자 목록 불러오기 실패:", err);
    message.error("관리자 목록을 불러오지 못했습니다.");
  } finally {
    setLoading(false);
  }
};


  useEffect(() => {
    fetchAdmins();
  }, []);

  /** 🔹 SUPER_ADMIN 권한 위임 */
  const handleTransfer = async (fromId: number, toId: number) => {
    if (!window.confirm(`관리자 #${toId}에게 SUPER_ADMIN 권한을 위임하시겠습니까?`)) return;

    try {
      await axios.post(
        `/api/super/admins/transfer?fromId=${fromId}&toId=${toId}`,
        null,
        { withCredentials: true }
      );

      message.success(`SUPER_ADMIN 권한이 관리자 #${toId}에게 위임되었습니다.`);
      fetchAdmins();
    } catch (err: any) {
      console.error("권한 위임 실패:", err);
      message.error(err.response?.data || "권한 위임에 실패했습니다.");
    }
  };

  /** 🔹 권한 변경 */
  const handleChangeRole = async (id: number, targetRole: string) => {
    if (!window.confirm(`이 관리자의 권한을 '${targetRole}'로 변경하시겠습니까?`)) return;

    try {
      await axios.patch(
        `/api/super/admins/${id}/role`,
        { role: targetRole },
        { withCredentials: true }
      );

      message.success(`관리자 #${id} 권한이 '${targetRole}'으로 변경되었습니다.`);
      setData((prev) =>
        prev.map((a) =>
          a.id === id ? { ...a, role: targetRole as any } : a
        )
      );
    } catch (err) {
      console.error("권한 변경 실패:", err);
      message.error("권한 변경에 실패했습니다.");
    }
  };

  /** 🔹 관리자 삭제 */
  const handleDelete = async (id: number, role: string) => {
    if (role === "SUPER_ADMIN") {
      return message.warning("SUPER_ADMIN 계정은 삭제할 수 없습니다.");
    }

    if (!window.confirm(`관리자 #${id}을(를) 삭제하시겠습니까?`)) return;

    try {
      await axios.delete(`/api/super/admins/${id}`, {
        withCredentials: true,
      });

      message.success(`관리자 #${id}이 삭제되었습니다.`);
      setData((prev) => prev.filter((a) => a.id !== id));
    } catch (err) {
      console.error("삭제 실패:", err);
      message.error("관리자 삭제에 실패했습니다.");
    }
  };

  /** 🔹 상세 보기 */
  const handleView = (admin: AdminUser) => {
    Modal.info({
      title: `🧑‍💼 관리자 #${admin.id} 상세 정보`,
      width: 500,
      content: (
        <Descriptions bordered column={1} size="small">
          <Descriptions.Item label="이메일">{admin.email}</Descriptions.Item>
          <Descriptions.Item label="닉네임">{admin.nickname}</Descriptions.Item>
          <Descriptions.Item label="권한">
            <Tag
              color={
                admin.role === "SUPER_ADMIN"
                  ? "green"
                  : admin.role === "ADMIN"
                  ? "red"
                  : "blue"
              }
            >
              {admin.role}
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="상태">
            <Tag
              color={
                admin.status === "ACTIVE"
                  ? "green"
                  : admin.status === "SUSPENDED"
                  ? "orange"
                  : "gray"
              }
            >
              {admin.status}
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="가입일">
            {admin.createdAt
              ? new Date(admin.createdAt).toLocaleString()
              : "—"}
          </Descriptions.Item>
        </Descriptions>
      ),
    });
  };

  /** 🔹 테이블 컬럼 정의 */
  const columns: ColumnsType<AdminUser> = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "이메일", dataIndex: "email", key: "email" },
    { title: "닉네임", dataIndex: "nickname", key: "nickname" },
    {
      title: "권한",
      dataIndex: "role",
      key: "role",
      render: (role: string) => (
        <Tag
          color={
            role === "SUPER_ADMIN"
              ? "green"
              : role === "ADMIN"
              ? "red"
              : "blue"
          }
        >
          {role}
        </Tag>
      ),
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      render: (status: string) => (
        <Tag
          color={
            status === "ACTIVE"
              ? "green"
              : status === "SUSPENDED"
              ? "orange"
              : "gray"
          }
        >
          {status}
        </Tag>
      ),
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
          <Button type="link" onClick={() => handleView(record)}>
            보기
          </Button>

          {/* USER → ADMIN */}
          {record.role === "USER" && (
            <Button
              type="link"
              onClick={() => handleChangeRole(record.id, "ADMIN")}
            >
              관리자로 승급
            </Button>
          )}

          {/* ADMIN → USER */}
          {record.role === "ADMIN" && (
            <Button
              type="link"
              danger
              onClick={() => handleChangeRole(record.id, "USER")}
            >
              일반으로 강등
            </Button>
          )}

          {/* SUPER_ADMIN → 위임 */}
          {record.role === "SUPER_ADMIN" && (
            <Button
              type="link"
              onClick={() => {
                const candidates = data.filter((a) => a.role === "ADMIN");
                if (candidates.length === 0) {
                  return message.warning("위임할 ADMIN 계정이 없습니다.");
                }

                Modal.info({
                  title: "위임할 ADMIN 선택",
                  width: 450,
                  content: (
                    <div>
                      {candidates.map((c) => (
                        <div key={c.id} style={{ marginBottom: 8 }}>
                          <Button
                            block
                            onClick={() => handleTransfer(record.id, c.id)}
                          >
                            #{c.id} {c.nickname} ({c.email})
                          </Button>
                        </div>
                      ))}
                    </div>
                  ),
                });
              }}
            >
              권한 위임
            </Button>
          )}

          {/* 삭제 */}
          <Button
            type="link"
            danger
            onClick={() => handleDelete(record.id, record.role)}
          >
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h2>🧑‍💼 관리자 관리</h2>

      <Table<AdminUser>
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 10 }}
      />
    </div>
  );
};

export default SuperAdminsPage;
