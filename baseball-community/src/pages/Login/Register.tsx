// src/pages/Register/Register.tsx
import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Register.css";
import { signup } from "../../api/user"; // signup API 불러오기 (경로 맞게 수정 필요)

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");
  const [passwordValid, setPasswordValid] = useState(false);
  const [nickname, setNickname] = useState("");
  const [emailMessage, setEmailMessage] = useState("");
  const [nicknameMessage, setNicknameMessage] = useState("");
  const navigate = useNavigate();
  // 이메일 중복확인
const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

const handleCheckEmail = async () => {
  if (!emailRegex.test(email)) {
    setEmailMessage("❌ 올바른 이메일 형식이 아닙니다.");
    return;
  }

  try {
    const res = await axios.get("/api/auth/check-email", { params: { email } });
    if (res.data) setEmailMessage("✅ 사용 가능한 이메일입니다.");
    else setEmailMessage("❌ 이미 사용 중인 이메일입니다.");
  } catch (err) {
    setEmailMessage("⚠️ 서버 오류가 발생했습니다.");
  }
};

  // 닉네임 중복확인
 const handleCheckNickname = async () => {
    try {
      const res = await axios.get("/api/auth/check-nickname", { params: { nickname } });
      if (res.data) setNicknameMessage("✅ 사용 가능한 닉네임입니다.");
      else setNicknameMessage("❌ 이미 사용 중인 닉네임입니다.");
    } catch (err) {
      setNicknameMessage("⚠️ 서버 오류가 발생했습니다.");
    }
  };
    // 정규식: 대문자, 소문자, 숫자, 특수문자 최소 1개씩, 8~20자
  const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,20}$/;

 const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const value = e.target.value;
  setPassword(value);

  if (!passwordRegex.test(value)) {
    setPasswordMessage("❌ 비밀번호는 8~20자, 대/소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
    setPasswordValid(false);
  } else {
    setPasswordMessage("✅ 사용 가능한 비밀번호입니다.");
    setPasswordValid(true);
  }
};

const handlePasswordCheckChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const value = e.target.value;
  setPasswordCheck(value);

  // 1️⃣ 규칙 먼저 검사
  if (!passwordRegex.test(password)) {
    setPasswordMessage("❌ 비밀번호는 8~20자, 대/소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
    setPasswordValid(false);
    return;
  }

  // 2️⃣ 규칙 통과 후 → 일치 여부 검사
  if (password !== value) {
    setPasswordMessage("❌ 비밀번호가 일치하지 않습니다.");
    setPasswordValid(false);
  } else {
    setPasswordMessage("✅ 비밀번호가 일치합니다.");
    setPasswordValid(true);
  }
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
      navigate("/");
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
  {emailMessage && (
  <p
    className={
      emailMessage.includes("✅") ? "message success" : "message error"
    }
  >
    {emailMessage}
  </p>
)}
    <div className="register__field">
      <input
        type="password"
        placeholder="비밀번호"
        className="register__input"
        value={password}
        onChange={handlePasswordChange}   //  비밀번호 유효성 검사
      />
    </div>

    <div className="register__field">
      <input
        type="password"
        placeholder="비밀번호 확인"
        className="register__input"
        value={passwordCheck}
        onChange={handlePasswordCheckChange}   //  비밀번호 일치 확인
      />
    </div>

{passwordMessage && (
  <p className={passwordMessage.includes("✅") ? "message success" : "message error"}>
    {passwordMessage}
  </p>
)}

      
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
        {nicknameMessage && (
          <p className={nicknameMessage.includes("✅") ? "message success" : "message error"}>
            {nicknameMessage}
          </p>
        )}


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
