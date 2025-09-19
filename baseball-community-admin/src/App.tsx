import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminLayout from "./layouts/AdminLayout";
import DashboardPage from "./features/dashboard/DashboardPage";
import UsersPage from "./features/users/UsersPage";
import PostsPage from "./features/posts/PostsPage";
import ReportsPage from "./features/reports/ReportsPage";
import NoticesPage from "./features/notices/NoticesPage";

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/" element={<AdminLayout />}>
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="users" element={<UsersPage />} />
          <Route path="posts" element={<PostsPage />} />
          <Route path="reports" element={<ReportsPage />} />
          <Route path="notices" element={<NoticesPage />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default App;
