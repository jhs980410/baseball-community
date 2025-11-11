import type { Report } from "./report";

export const mockReportsComments: Report[] = [
  {
    id: 10,
    target_type: "comment",
    target_id: 501,
    user_id: 12,
    reason: "spam",
    status: "pending",
    created_at: "2025-11-11 11:05:00",
  },
  {
    id: 11,
    target_type: "comment",
    target_id: 502,
    user_id: 15,
    reason: "abuse",
    status: "reviewed",
    created_at: "2025-11-10 13:30:00",
  },
  {
    id: 12,
    target_type: "comment",
    target_id: 503,
    user_id: 18,
    reason: "personal_info",
    status: "resolved",
    created_at: "2025-11-08 21:20:00",
  },
];
