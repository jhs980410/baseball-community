import React, { useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

export default function Editor() {
  // value는 Quill이 반환하는 HTML string
  const [value, setValue] = useState<string>("");

  return (
    <div>
      <ReactQuill
        theme="snow"
        value={value}
        onChange={(content: string) => setValue(content)} // 타입 명시
        placeholder="내용을 입력하세요..."
        style={{ height: "300px", marginBottom: "50px" }}
      />
      <pre>{value}</pre> {/* 디버깅용: 작성된 HTML 출력 */}
    </div>
  );
}