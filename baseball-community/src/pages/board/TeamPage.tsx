// src/pages/Board/TeamPage.tsx
import { useParams } from "react-router-dom";
import Posts from "../Posts/Posts";
import SideBar from "../../components/SideBar/SideBar";
import "./TeamPage.css";
export default function TeamPage() {
  const { id } = useParams(); // 예: doosan, lg, ssg...

  return (
    <main className="main-content team-page">
      {/* 게시글 목록 */}
      <Posts teamId={id} />

      {/* 사이드바 */}
      <SideBar />
    </main>
  );
}
