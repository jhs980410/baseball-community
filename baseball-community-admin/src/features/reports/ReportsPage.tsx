import React from "react";
import { Tabs } from "antd";
import ReportsPosts from "./ReportsPosts";
import ReportsComments from "./ReportsComments";
import ReportsUsers from "./ReportsUsers";

const ReportsPage: React.FC = () => {
  const items = [
    { key: "posts", label: "📄 신고된 게시글", children: <ReportsPosts /> },
    { key: "comments", label: "💬 신고된 댓글", children: <ReportsComments /> },
    { key: "users", label: "🙍 신고된 사용자", children: <ReportsUsers /> },
  ];

  return (
    <div style={{ padding: 20 }}>
      <h2>🚨 신고 관리</h2>
      <Tabs defaultActiveKey="posts" items={items} />
    </div>
  );
};

export default ReportsPage;
