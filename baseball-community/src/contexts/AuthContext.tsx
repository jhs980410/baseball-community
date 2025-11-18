import React, { createContext, useState, ReactNode, useEffect } from "react";
import axios from "axios";

export interface UserInfo {
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

  useEffect(() => {
    axios
      .get<UserInfo>("/api/auth/me", { withCredentials: true })
      .then((res) => {
        if (!res.data.role) {
          setUserInfo(null);
          return;
        }
        setUserInfo(res.data);
      })
      .catch(() => setUserInfo(null));
  }, []);

  return (
    <AuthContext.Provider value={{ userInfo, setUserInfo }}>
      {children}
    </AuthContext.Provider>
  );
}

export default AuthProvider;
