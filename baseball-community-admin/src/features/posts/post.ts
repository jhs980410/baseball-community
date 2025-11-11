export interface PostStatus {
  likeCount: number;
  commentCount: number;
  viewCount: number;
  reportCount: number;
  containsBannedWord: boolean;
  flagged: boolean;
  lastFlagReason?: string;
}

export interface Post {
  id: number;
  title: string;
  content?: string;
  userId: number;
  teamId: number;
  isHidden: boolean;
  status: PostStatus;
}
