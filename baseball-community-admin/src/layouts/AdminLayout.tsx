import React, { useState } from "react";
import { Layout, Menu } from "antd";
import {
  DashboardOutlined,
  UserOutlined,
  FileTextOutlined,
  ExclamationCircleOutlined,
  NotificationOutlined,
} from "@ant-design/icons";
import { Link, Outlet } from "react-router-dom";

const { Header, Sider, Content } = Layout;

const AdminLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);

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
            <Link to="/dashboard">Dashboard</Link>
          </Menu.Item>
          <Menu.Item key="users" icon={<UserOutlined />}>
            <Link to="/users">Users</Link>
          </Menu.Item>
          <Menu.Item key="posts" icon={<FileTextOutlined />}>
            <Link to="/posts">Posts</Link>
          </Menu.Item>
          <Menu.Item key="reports" icon={<ExclamationCircleOutlined />}>
            <Link to="/reports">Reports</Link>
          </Menu.Item>
          <Menu.Item key="notices" icon={<NotificationOutlined />}>
            <Link to="/notices">Notices</Link>
          </Menu.Item>
        </Menu>
      </Sider>

      {/* 메인 레이아웃 */}
      <Layout>
        <Header style={{ background: "#fff", padding: 0, textAlign: "right", paddingRight: 20 }}>
          <span>관리자님</span> | <a href="/logout">Logout</a>
        </Header>
        <Content style={{ margin: "16px" }}>
          <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
            <Outlet /> {/* 라우터 페이지가 여기에 렌더링 */}
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout;
