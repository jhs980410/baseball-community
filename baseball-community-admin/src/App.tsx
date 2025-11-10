import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminLayout from "./layouts/AdminLayout";
import DashboardPage from "./features/dashboard/DashboardPage";
import UsersPage from "./features/users/UsersPage";
import PostsPage from "./features/posts/PostsPage";
import ReportsPage from "./features/reports/ReportsPage";
import NoticesPage from "./features/notices/NoticesPage";
import AdminLoginPage from "./features/users/AdminLoginPage";
import SuperAdminsPage from "./features/users/SuperAdminsPage";

const App: React.FC = () => {
  return (
<Router>
  <Routes>
    {/* 기본 루트 → 관리자 로그인 페이지 */}
    <Route path="/" element={<Navigate to="/admin/auth" replace />} />

    {/* 로그인 페이지만 별도 (레이아웃 없음) */}
    <Route path="/admin/auth" element={<AdminLoginPage />} />

    {/* 관리자 레이아웃 + 내부 페이지 */}
    <Route path="/admin" element={<AdminLayout />}>
      <Route path="dashboard" element={<DashboardPage />} />
      <Route path="users" element={<UsersPage />} />
      <Route path="posts" element={<PostsPage />} />
      <Route path="reports" element={<ReportsPage />} />
      <Route path="notices" element={<NoticesPage />} />
      <Route path="super-admins" element={<SuperAdminsPage />} />
    </Route>
  </Routes>
</Router>
  );
};

export default App;
