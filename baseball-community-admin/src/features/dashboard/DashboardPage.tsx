import React, { useEffect, useState } from "react";
import axios from "axios";
import { Row, Col, Card, Statistic, Table, List, Tag, Spin } from "antd";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Legend,
} from "recharts";
import type { DashboardResponse } from "./dashboard"; // alias 대신 상대경로

const DashboardPage: React.FC = () => {
  const [data, setData] = useState<DashboardResponse | null>(null);
  const [loading, setLoading] = useState(true);

useEffect(() => {
  const fetchDashboard = async () => {
    setLoading(true);
    try {
      const res: any = await axios.get("/api/admin/dashboards", {
        withCredentials: true,
      });

      console.log("📡 Dashboard API 응답:", res.data);
      setData(res.data);

    } catch (err) {
      console.error("대시보드 데이터 불러오기 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  fetchDashboard();
}, []);


  if (loading)
    return (
      <Spin size="large" style={{ display: "block", margin: "50px auto" }} />
    );
  if (!data) return <p>데이터를 불러올 수 없습니다.</p>;

  // 신고 현황 집계
  const reportData = [
    { name: "스팸", value: data.reports.filter((r) => r.reason === "SPAM").length },
    { name: "욕설/비방", value: data.reports.filter((r) => r.reason === "ABUSE").length },
    { name: "성인", value: data.reports.filter((r) => r.reason === "ADULT").length },
    { name: "개인정보", value: data.reports.filter((r) => r.reason === "PERSONAL_INFO").length },
  ];
 
  const COLORS = ["#FF6384", "#36A2EB", "#FFCE56", "#8BC34A"];

  const postColumns = [
    { title: "ID", dataIndex: "id", key: "id", width: 60 },
    {
      title: "제목",
      dataIndex: "title",
      key: "title",
      render: (text: string) => <a>{text}</a>,
    },
    { title: "작성자(ID)", dataIndex: "userId", key: "userId" },
    { title: "팀(ID)", dataIndex: "teamId", key: "teamId" },
  ];

  return (
    <div>
      <h2>📊 Dashboard</h2>

      {/* 요약 통계 */}
      <Row gutter={16}>
        <Col span={8}>
          <Card>
            <Statistic title="전체 회원 수" value={data.totalUsers} />
          </Card> 인기글 Top 5
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="전체 게시글 수" value={data.totalPosts} />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="신고 건수" value={data.totalReports} />
          </Card>
        </Col>
      </Row>

      {/* 일일 활동 통계 */}
      <Card style={{ marginTop: 20 }}>
        <h3>📅 일일 활동 통계</h3>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={data.dailyStats}>
            <XAxis dataKey="statDate" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="newUsers" stroke="#8884d8" name="신규회원" />
            <Line type="monotone" dataKey="newPosts" stroke="#82ca9d" name="게시글" />
            <Line type="monotone" dataKey="newComments" stroke="#ffc658" name="댓글" />
          </LineChart>
        </ResponsiveContainer>
      </Card>

      {/* 인기글 */}
      <Card style={{ marginTop: 20 }}>
        <h3>🔥 인기글 Top 5</h3>
        <Table columns={postColumns} dataSource={data.topPosts} pagination={false} rowKey="id" />
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
          dataSource={data.adminLogs}
          renderItem={(log) => (
            <List.Item>
              <Tag color="blue">{log.action}</Tag>
              {log.targetType} #{log.targetId} (by Admin {log.adminId})
            </List.Item>
          )}
        />
      </Card>
    </div>
  );
};

export default DashboardPage;
