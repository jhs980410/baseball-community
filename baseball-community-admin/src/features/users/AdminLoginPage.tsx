import React, { useState } from "react";
import { Card, Form, Input, Button, message } from "antd";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const AdminLoginPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  //  로그인 처리 함수
  const handleLogin = async (values: { email: string; password: string }) => {
    setLoading(true);
    try {
      // 백엔드 컨트롤러 경로: /api/admin/auth/login
      const res = await axios.post("/api/admin/auth/login", values, {
        withCredentials: true, // HttpOnly 쿠키로 JWT 수신
      });

      if (res.status === 200) {
        message.success("✅ 관리자 로그인 성공");
        console.log("관리자 로그인 성공:", res.data);

        // JWT 토큰이 쿠키에 저장되었으므로 바로 대시보드로 이동
        navigate("/admin/dashboard", { replace: true });
      } else {
        message.error("로그인 실패: 서버 응답이 올바르지 않습니다.");
      }
    } catch (err: any) {
      console.error("관리자 로그인 실패:", err);
      message.error("이메일 또는 비밀번호를 확인하세요.");
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
              { type: "email", message: "유효한 이메일 주소가 아닙니다." },
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
