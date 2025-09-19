import type {  Report } from "../../types/report";

export const mockReports: Report[] = [
  {
    id: 1,
    target_type: "post",
    target_id: 5,
    user_id: 3,
    reason: "spam",
    status: "pending",
    created_at: "2025-09-12 14:20:00",
  },
  {
    id: 2,
    target_type: "comment",
    target_id: 8,
    user_id: 4,
    reason: "abuse",
    status: "reviewed",
    created_at: "2025-09-12 18:45:00",
  },
  {
    id: 3,
    target_type: "post",
    target_id: 12,
    user_id: 7,
    reason: "adult",
    status: "resolved",
    created_at: "2025-09-13 09:10:00",
  },
  {
    id: 4,
    target_type: "comment",
    target_id: 15,
    user_id: 8,
    reason: "personal_info",
    status: "pending",
    created_at: "2025-09-13 11:30:00",
  },
  {
    id: 5,
    target_type: "post",
    target_id: 20,
    user_id: 10,
    reason: "spam",
    status: "pending",
    created_at: "2025-09-14 15:50:00",
  },
  // 6 ~ 20
  ...Array.from({ length: 15 }, (_, i) => ({
  id: i + 6,
  target_type: (i % 2 === 0 ? "post" : "comment") as Report["target_type"],
  target_id: (i + 3) * 2,
  user_id: (i % 10) + 1,
  reason: (["spam", "abuse", "adult", "personal_info"] as const)[i % 4],
  status: (["pending", "reviewed", "resolved"] as const)[i % 3],
  created_at: `2025-09-${15 + i} 13:00:00`,
})),

];
