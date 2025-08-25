// src/pages/Register/Register.tsx
import React from "react";
import "../../styles/Register.css";

export default function Register() {
  return (
    <div className="register-container">
      <div className="register-box">
        <h2>회원가입</h2>
        <input type="email" placeholder="이메일 주소" />
        <input type="password" placeholder="비밀번호" />
        <input type="password" placeholder="비밀번호 확인" />
        <input type="text" placeholder="닉네임" />
        <button className="btn-submit">가입하기</button>
        <div className="links">
          <a href="#">이용약관</a> | <a href="#">개인정보 처리방침</a>
        </div>
      </div>
    </div>
  );
}
