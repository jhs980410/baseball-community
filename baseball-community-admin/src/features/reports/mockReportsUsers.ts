import type { Report } from "./report";

export const mockReportsUsers: Report[] = [
  {
    id: 21,
    target_type: "user",
    target_id: 2001,
    user_id: 9,
    reason: "abuse",
    status: "pending",
    created_at: "2025-11-11 10:00:00",
  },
  {
    id: 22,
    target_type: "user",
    target_id: 2002,
    user_id: 6,
    reason: "spam",
    status: "reviewed",
    created_at: "2025-11-09 19:45:00",
  },
  {
    id: 23,
    target_type: "user",
    target_id: 2003,
    user_id: 5,
    reason: "adult",
    status: "resolved",
    created_at: "2025-11-07 08:10:00",
  },
];
