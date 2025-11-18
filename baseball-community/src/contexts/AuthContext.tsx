import React, { createContext, useState, ReactNode, useEffect } from "react";
import axios from "axios";

interface UserInfo {
  id: number;
  email: string;
  nickname: string;
  role: string;
}

interface AuthContextType {
  userInfo: UserInfo | null;
  setUserInfo: React.Dispatch<React.SetStateAction<UserInfo | null>>;
}

export const AuthContext = createContext<AuthContextType>({
  userInfo: null,
  setUserInfo: () => {},
});

export function AuthProvider({ children }: { children: ReactNode }) {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);

  // 🔥 앱이 처음 로드될 때 자동 로그인 복원
 useEffect(() => {
  axios
    .get<UserInfo>("/api/auth/me", { withCredentials: true })
    .then((res) => {
      setUserInfo(res.data);  // 타입 안정적
    })
    .catch(() => setUserInfo(null));
}, []);

  return (
    <AuthContext.Provider value={{ userInfo, setUserInfo }}>
      {children}
    </AuthContext.Provider>
  );
}
