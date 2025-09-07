import React from "react";
import Editor from "../../components/Editor/Editor";

export default function BoardWrite() {
  return (
    <div>
      <h2>게시글 작성</h2>
      <Editor />
      <button>등록하기</button>
    </div>
  );
}
