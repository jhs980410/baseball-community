import React, { useState, ReactNode, useEffect } from "react";
import axios from "axios";
import { AuthContext, UserInfo } from "./AuthContext";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);

  useEffect(() => {
    axios
      .get<UserInfo>("/api/auth/me", { withCredentials: true })
      .then((res) => {
        // 🔥 서버가 role 안 준 상황 대비
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
