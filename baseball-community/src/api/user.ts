import axios from "axios";

const API_URL = "/api/auth";
export interface LoginResponse {
  id: number;
  email: string;
  nickname: string;
  role: string;
}


// 회원가입
export const signup = async (data: { email: string; password: string; nickname: string }) => {
  const res = await axios.post(`${API_URL}/signup`, data);
  return res.data;
};

// 로그인
export const login = async (
  data: { email: string; password: string }
): Promise<LoginResponse> => {
  const res = await axios.post<LoginResponse>(`${API_URL}/login`, data, {
    withCredentials: true, // 쿠키 자동 저장
  });
  return res.data; // { id, email, nickname }
};

// 내 정보 조회
// 예시: 프로필 조회
export const getProfile = async () => {
  const res = await axios.get("/api/users/me", {
    withCredentials: true, // 쿠키 자동 전송
  });
  return res.data;
};