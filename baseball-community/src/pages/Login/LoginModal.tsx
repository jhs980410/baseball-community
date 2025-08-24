import React from "react";
import ReactDOM from "react-dom";
import "../../styles/LoginModal.css";

interface Props {
  onClose: () => void;
}
export default function LoginDropdown({ onClose }: Props) {
  return (
    <div className="login-dropdown" onClick={(e) => e.stopPropagation()}>
      <h3>로그인</h3>
      <input type="email" placeholder="이메일" />
      <input type="password" placeholder="비밀번호" />
      <button className="login-btn">로그인</button>
      <div className="links">
        <a href="#">비밀번호 찾기</a> | <a href="#">회원가입</a>
      </div>
    </div>
  );
}
