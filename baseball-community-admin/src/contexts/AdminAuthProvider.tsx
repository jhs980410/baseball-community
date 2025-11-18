import React, { useState, ReactNode, useEffect } from "react";
import axios from "axios";
import { AdminAuthContext, AdminInfo } from "./AdminAuthContext";

export function AdminAuthProvider({ children }: { children: ReactNode }) {
  const [admin, setAdmin] = useState<AdminInfo | null>(null);

  useEffect(() => {
    axios
      .get<AdminInfo>("/api/admin/auth/me", { withCredentials: true })
      .then((res) => {
        if (res.data.role === "ADMIN" || res.data.role === "SUPER_ADMIN") {
          setAdmin(res.data);
        } else {
          setAdmin(null);
        }
      })
      .catch(() => setAdmin(null));
  }, []);

  return (
    <AdminAuthContext.Provider value={{ admin, setAdmin }}>
      {children}
    </AdminAuthContext.Provider>
  );
}
