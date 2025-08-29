import axios from "axios";

const API_URL = "/api/auth";

export interface LoginResponse {
  token: string;
  email: string;
  nickname: string;
}




// 회원가입
export const signup = async (data: { email: string; password: string; nickname: string }) => {
  const res = await axios.post(`${API_URL}/signup`, data);
  return res.data;
};

// 로그인
export const login = async (data: { email: string; password: string }) => {
  const res = await axios.post(`${API_URL}/login`, data, { withCredentials: true });
  return res.data; // { token, userInfo }
};  

// 내 정보 조회
export const getProfile = async (token: string) => {
  const res = await axios.get(`${API_URL}/me`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};
