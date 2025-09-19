export interface Post {
  id: number;
  user_id: number;
  team_id: number;
  title: string;
  content: string;
  is_hidden: boolean;
  created_at: string;
  updated_at: string;
}
