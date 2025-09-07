import React, { useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css"; // 기본 스타일

export default function Editor() {
  const [value, setValue] = useState("");

  return (
    <div>
      <ReactQuill
        theme="snow"
        value={value}
        onChange={setValue}
        placeholder="내용을 입력하세요..."
        style={{ height: "300px", marginBottom: "50px" }}
      />
      <pre>{value}</pre> {/* 디버깅용: 작성된 HTML 출력 */}
    </div>
  );
}
