// src/types/admin/dashboard.ts

export interface DailyStats {
  statDate: string;
  newUsers: number;
  newPosts: number;
  newComments: number;
}

export interface AdminPost {
  id: number;
  title: string;
  userId: number;
  teamId: number;
}

export interface AdminReport {
  id: number;
  reason: string;
  targetType: string;
  targetId: number;
  reporterId: number;
  createdAt: string;
}

export interface AdminLog {
  id: number;
  action: string;
  targetType: string;
  targetId: number;
  adminId: number;
  createdAt: string;
}

export interface DashboardResponse {
  totalUsers: number;
  totalPosts: number;
  totalReports: number;
  dailyStats: DailyStats[];
  topPosts: AdminPost[];
  reports: AdminReport[];
  adminLogs: AdminLog[];
}
