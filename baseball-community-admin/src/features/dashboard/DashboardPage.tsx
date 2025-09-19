import React from "react";
import { Row, Col, Card, Statistic, Table, List, Tag } from "antd";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, LineChart, Line, XAxis, YAxis, Legend } from "recharts";
import { mockPosts } from "../posts/mockPosts";
import { mockReports } from "../reports/mockReports";
import { mockDailyStats } from "../stats/mockDailyStats";
import { mockAdminLogs } from "../logs/mockAdminLogs";

const DashboardPage: React.FC = () => {
  // 요약 통계
  const totalUsers = 120;
  const totalPosts = mockPosts.length;
  const totalReports = mockReports.length;

  // 신고 현황 집계
  const reportData = [
    { name: "스팸", value: mockReports.filter(r => r.reason === "spam").length },
    { name: "욕설/비방", value: mockReports.filter(r => r.reason === "abuse").length },
    { name: "성인", value: mockReports.filter(r => r.reason === "adult").length },
    { name: "개인정보", value: mockReports.filter(r => r.reason === "personal_info").length },
  ];

  const COLORS = ["#FF6384", "#36A2EB", "#FFCE56", "#8BC34A"];

  // 인기글 Top 5
  const topPosts = mockPosts
    .slice(0, 5)
    .map(p => ({ ...p, key: p.id }));

  const postColumns = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    { 
      title: "제목", 
      dataIndex: "title", 
      key: "title",
      render: (text: string) => <a>{text}</a>,
    },
    { title: "작성자(ID)", dataIndex: "user_id", key: "user_id" },
    { title: "팀(ID)", dataIndex: "team_id", key: "team_id" },
  ];

  return (
    <div>
      <h2>📊 Dashboard</h2>

      {/* 요약 통계 */}
      <Row gutter={16}>
        <Col span={8}>
          <Card><Statistic title="전체 회원 수" value={totalUsers} /></Card>
        </Col>
        <Col span={8}>
          <Card><Statistic title="전체 게시글 수" value={totalPosts} /></Card>
        </Col>
        <Col span={8}>
          <Card><Statistic title="신고 건수" value={totalReports} /></Card>
        </Col>
      </Row>

      {/* 일일 활동 통계 */}
      <Card style={{ marginTop: 20 }}>
        <h3>📅 일일 활동 통계</h3>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={mockDailyStats}>
            <XAxis dataKey="stat_date" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="new_users" stroke="#8884d8" name="신규회원" />
            <Line type="monotone" dataKey="new_posts" stroke="#82ca9d" name="게시글" />
            <Line type="monotone" dataKey="new_comments" stroke="#ffc658" name="댓글" />
          </LineChart>
        </ResponsiveContainer>
      </Card>

      {/* 인기글 */}
      <Card style={{ marginTop: 20 }}>
        <h3>🔥 인기글 Top 5</h3>
        <Table columns={postColumns} dataSource={topPosts} pagination={false} />
      </Card>

      {/* 신고 현황 */}
      <Card style={{ marginTop: 20 }}>
        <h3>🚨 신고 현황</h3>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={reportData}
              dataKey="value"
              nameKey="name"
              outerRadius={120}
              fill="#8884d8"
              label
            >
              {reportData.map((_, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </Card>

      {/* 관리자 로그 */}
      <Card style={{ marginTop: 20 }}>
        <h3>🛡 최근 관리자 로그</h3>
        <List
          dataSource={mockAdminLogs}
          renderItem={log => (
            <List.Item>
              <Tag color="blue">{log.action}</Tag>
              {log.target_type} #{log.target_id} (by Admin {log.admin_id})
            </List.Item>
          )}
        />
      </Card>
    </div>
  );
};

export default DashboardPage;
