export interface Report {
  id: number;
  target_type: "post" | "comment" | "user";
  target_id: number;
  user_id: number;
  reason: "spam" | "abuse" | "adult" | "personal_info";
  status: "pending" | "reviewed" | "resolved";
  created_at: string;
}