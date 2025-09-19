export interface User {
  id: number;
  email: string;
  nickname: string;
  role: "USER" | "ADMIN" | "MODERATOR";
  status: "ACTIVE" | "SUSPENDED" | "DELETED";
  created_at: string;
}