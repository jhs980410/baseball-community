import React, { useState } from "react";
import { Layout, Menu } from "antd";
import {
  DashboardOutlined,
  UserOutlined,
  FileTextOutlined,
  ExclamationCircleOutlined,
  NotificationOutlined,
} from "@ant-design/icons";
import { Link, Outlet, useNavigate } from "react-router-dom";
import axios from "axios";

const { Header, Sider, Content } = Layout;

const AdminLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();

  //  로그아웃 함수
const handleLogout = async () => {
  try {
    await axios.delete("/api/admin/auth/logout", { withCredentials: true });
    navigate("/"); // 로그아웃 후 메인으로 이동
  } catch (error) {
    console.error("로그아웃 실패:", error);
  }
};
  return (
    <Layout style={{ minHeight: "100vh" }}>
      {/* 사이드바 */}
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <div
          style={{
            height: 32,
            margin: 16,
            background: "rgba(255, 255, 255, 0.3)",
          }}
        />
        <Menu theme="dark" mode="inline" defaultSelectedKeys={["dashboard"]}>
          <Menu.Item key="dashboard" icon={<DashboardOutlined />}>
            <Link to="./dashboard">Dashboard</Link>
          </Menu.Item>
          <Menu.Item key="users" icon={<UserOutlined />}>
            <Link to="./users">Users</Link>
          </Menu.Item>
          <Menu.Item key="posts" icon={<FileTextOutlined />}>
            <Link to="./posts">Posts</Link>
          </Menu.Item>
          <Menu.Item key="reports" icon={<ExclamationCircleOutlined />}>
            <Link to="./reports">Reports</Link>
          </Menu.Item>
          <Menu.Item key="notices" icon={<NotificationOutlined />}>
            <Link to="./notices">Notices</Link>
          </Menu.Item>
        </Menu>
      </Sider>

      {/* 메인 레이아웃 */}
      <Layout>
        <Header
          style={{
            background: "#fff",
            padding: 0,
            textAlign: "right",
            paddingRight: 20,
          }}
        >
          <span>관리자님</span> |{" "}
          {/* 🔹 클릭 시 로그아웃 함수 실행 */}
          <button
            onClick={handleLogout}
            style={{
              border: "none",
              background: "transparent",
              color: "#1890ff",
              cursor: "pointer",
            }}
          >
            Logout
          </button>
        </Header>
        <Content style={{ margin: "16px" }}>
          <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
            <Outlet /> {/* 하위 라우터 렌더링 */}
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout;
