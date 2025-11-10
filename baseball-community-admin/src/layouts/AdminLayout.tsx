import React, { useEffect, useState } from "react";
import { Layout, Menu } from "antd";
import {
  DashboardOutlined,
  UserOutlined,
  FileTextOutlined,
  ExclamationCircleOutlined,
  NotificationOutlined,
  CrownOutlined,
} from "@ant-design/icons";
import { Link, Outlet, useNavigate } from "react-router-dom";
import axios from "axios";

const { Header, Sider, Content } = Layout;

const AdminLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [role, setRole] = useState<string | null>(localStorage.getItem("role")); //  초기값 바로 반영
  const navigate = useNavigate();

  useEffect(() => {
    //  role 값이 비어있을 경우 주기적으로 다시 확인
    const checkRole = () => {
      const storedRole = localStorage.getItem("role");
      if (storedRole && storedRole !== role) {
        setRole(storedRole);
        console.log("현재 로그인 권한 갱신:", storedRole);
      } else if (!storedRole) {
        console.warn("role이 아직 설정되지 않음, 재확인 중...");
        setTimeout(checkRole, 200);
      }
    };

    checkRole();
  }, [role]);

  const handleLogout = async () => {
    try {
      await axios.delete("/api/admin/auth/logout", { withCredentials: true });
      localStorage.removeItem("role");
      navigate("/");
    } catch (error) {
      console.error("로그아웃 실패:", error);
    }
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <div style={{ height: 32, margin: 16, background: "rgba(255, 255, 255, 0.3)" }} />
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

          {/* 👑 SUPER_ADMIN 전용 메뉴 */}
          {role?.includes("SUPER_ADMIN") && (
            <Menu.Item key="super-admins" icon={<CrownOutlined />}>
              <Link to="./super-admins">Admin Management</Link>
            </Menu.Item>
          )}
        </Menu>
      </Sider>

      <Layout>
        <Header style={{ background: "#fff", textAlign: "right", paddingRight: 20 }}>
          <span>{role?.includes("SUPER_ADMIN") ? "슈퍼관리자님" : "관리자님"}</span> |{" "}
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
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout;
