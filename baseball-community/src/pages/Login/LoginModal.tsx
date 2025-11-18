import React, { useState, useContext } from "react";
import { Link } from "react-router-dom";
import "./LoginModal.css";
import { login } from "../../api/user"; 
import { AuthContext } from "../../contexts/AuthContext";

interface Props {
  onClose: () => void;
}

export default function LoginDropdown({ onClose }: Props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  
  //  전역 상태 가져오기
  const { setUserInfo } = useContext(AuthContext);

  const handleLogin = async () => {
    try {
      const res = await login({ email, password });

      //  로그인 성공 시 전역 상태 업데이트
                setUserInfo({
          id: res.id,
          email: res.email,
          nickname: res.nickname,
          role: res.role,
        });

      alert("로그인 성공!");
      onClose();
    } catch (err) {
      console.error("로그인 실패:", err);
      alert("로그인 실패. 이메일/비밀번호를 확인하세요.");
    }
  };

  return (
    <div className="login-dropdown" onClick={(e) => e.stopPropagation()}>
      <h3>로그인</h3>
      <input
        type="email"
        placeholder="이메일"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button className="login-btn" onClick={handleLogin}>
        로그인
      </button>
      <div className="links">
        <a href="#">비밀번호 찾기</a> | <Link to="/register">회원가입</Link>
      </div>
    </div>
  );
}
