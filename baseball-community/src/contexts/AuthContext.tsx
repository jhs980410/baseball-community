import { createContext } from "react";

export interface UserInfo {
  id: number;
  email: string;
  nickname: string;
  role: "USER" | "ADMIN" | "SUPER_ADMIN"; // 🔥 문자열 → 리터럴 타입
}

export interface AuthContextType {
  userInfo: UserInfo | null;
  setUserInfo: React.Dispatch<React.SetStateAction<UserInfo | null>>;
}

export const AuthContext = createContext<AuthContextType>({
  userInfo: null,
  setUserInfo: () => {},
});
