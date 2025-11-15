import React, { useState } from "react";
import { Card, Form, Input, Button, message } from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface AdminLoginResponse {
  role: "ADMIN" | "SUPER_ADMIN";
  email: string;
  nickname: string;
}

const AdminLoginPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  /** 🔐 관리자 로그인 */
  const handleLogin = async (values: { email: string; password: string }) => {
    setLoading(true);

    try {
      const res = await axios.post<AdminLoginResponse>(
        "/api/admin/auth/login",
        values,
        { withCredentials: true }
      );

      if (!res.data) {
        message.error("서버에서 로그인 정보를 받지 못했습니다.");
        return;
      }

      const { role, email, nickname } = res.data;

      message.success("✅ 관리자 로그인 성공");

      // localStorage 저장
      localStorage.setItem("role", role);
      localStorage.setItem("email", email);
      localStorage.setItem("nickname", nickname);

      console.log("저장된 관리자 정보:", role, email, nickname);

      // 짧은 지연 후 페이지 이동
      setTimeout(() => {
        navigate("/admin/dashboard", { replace: true });
      }, 100);
    } catch (err: any) {
      console.error("관리자 로그인 실패:", err);
      message.error("❌ 이메일 또는 비밀번호가 올바르지 않습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "#f0f2f5",
      }}
    >
      <Card
        title="🔐 관리자 로그인"
        style={{
          width: 380,
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          borderRadius: 12,
        }}
      >
        <Form layout="vertical" onFinish={handleLogin}>
          <Form.Item
            label="이메일"
            name="email"
            rules={[
              { required: true, message: "이메일을 입력하세요." },
              { type: "email", message: "유효한 이메일 형식이 아닙니다." },
            ]}
          >
            <Input placeholder="admin@example.com" />
          </Form.Item>

          <Form.Item
            label="비밀번호"
            name="password"
            rules={[{ required: true, message: "비밀번호를 입력하세요." }]}
          >
            <Input.Password placeholder="비밀번호" />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={loading}
              style={{ borderRadius: 6 }}
            >
              로그인
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default AdminLoginPage;
