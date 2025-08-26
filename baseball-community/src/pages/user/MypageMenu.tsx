import React, { useState, useRef } from "react";
import useOutsideClick from "../../hooks/useOutsideClick";
import "./MypageModal.css";

export default function MypageMenu() {
  const [showMenu, setShowMenu] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  useOutsideClick(menuRef, () => setShowMenu(false));

  return (
    <div className="mypage" ref={menuRef}>
      <button onClick={() => setShowMenu(!showMenu)}>마이페이지</button>
      {showMenu && (
        <div className="mypage-dropdown">
          <ul>
            <li>쪽지함</li>
            <li>내가 쓴 글</li>
            <li>내가 쓴 댓글</li>
            <li>프로필 수정</li>
            <li>로그아웃</li>
          </ul>
        </div>
      )}
    </div>
  );
}
