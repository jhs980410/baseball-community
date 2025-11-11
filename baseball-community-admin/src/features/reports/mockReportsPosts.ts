// src/features/reports/mock/mockReportsPosts.ts
import type { Report } from "./report";

export const mockReportsPosts: Report[] = [
  {
    id: 1,
    target_type: "post",
    target_id: 101,
    user_id: 5,
    reason: "abuse",
    status: "pending",
    created_at: "2025-11-11 14:30:00",
  },
  {
    id: 2,
    target_type: "post",
    target_id: 102,
    user_id: 7,
    reason: "spam",
    status: "reviewed",
    created_at: "2025-11-10 09:12:00",
  },
  {
    id: 3,
    target_type: "post",
    target_id: 103,
    user_id: 8,
    reason: "adult",
    status: "resolved",
    created_at: "2025-11-09 18:40:00",
  },
];
