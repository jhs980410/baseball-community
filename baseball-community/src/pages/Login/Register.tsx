// src/pages/Register/Register.tsx
import React, { useState } from "react";
import "./Register.css";
import { signup } from "../../api/user"; // signup API 불러오기 (경로 맞게 수정 필요)

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [nickname, setNickname] = useState("");

  // 이메일 중복확인
  const handleCheckEmail = () => {
    alert(`이메일 중복확인 요청: ${email}`);
    // TODO: axios.get("/api/auth/check-email", { params: { email } })
  };

  // 닉네임 중복확인
  const handleCheckNickname = () => {
    alert(`닉네임 중복확인 요청: ${nickname}`);
    // TODO: axios.get("/api/auth/check-nickname", { params: { nickname } })
  };

  // 회원가입
  const handleRegister = async () => {
    if (password !== passwordCheck) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }
    try {
      const res = await signup({ email, password, nickname });
      alert("회원가입 성공!");
      console.log(res);
    } catch (err: any) {
      alert("회원가입 실패: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="register-container">
      <div className="register-box">
        <h2>회원가입</h2>
        <div className="register__field">
          <input
            type="email"
            placeholder="이메일 주소"
            className="register__input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button className="register__btn" onClick={handleCheckEmail}>
            중복확인
          </button>
        </div>

        <div className="register__field">
          <input
            type="password"
            placeholder="비밀번호"
            className="register__input"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div className="register__field">
          <input
            type="password"
            placeholder="비밀번호 확인"
            className="register__input"
            value={passwordCheck}
            onChange={(e) => setPasswordCheck(e.target.value)}
          />
        </div>

        <div className="register__field">
          <input
            type="text"
            placeholder="닉네임"
            className="register__input"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
          />
          <button className="register__btn" onClick={handleCheckNickname}>
            중복확인
          </button>
        </div>

        <button className="btn-submit" onClick={handleRegister}>
          가입하기
        </button>
        <div className="links">
          <a href="#">이용약관</a> | <a href="#">개인정보 처리방침</a>
        </div>
      </div>
    </div>
  );
}
