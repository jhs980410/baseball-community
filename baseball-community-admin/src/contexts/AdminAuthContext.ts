import { createContext } from "react";

export interface AdminInfo {
  id: number;
  email: string;
  nickname: string;
  role: "ADMIN" | "SUPER_ADMIN";
}

export interface AdminAuthContextType {
  admin: AdminInfo | null;
  setAdmin: React.Dispatch<React.SetStateAction<AdminInfo | null>>;
}

export const AdminAuthContext = createContext<AdminAuthContextType>({
  admin: null,
  setAdmin: () => {},
});
