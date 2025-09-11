import { useLocation } from "react-router-dom";
import Posts from "../Posts/Posts";
import SideBar from "../../components/SideBar/SideBar";
import "./TeamPage.css";

export default function SearchPage() {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const type = params.get("type") || "all";
  const keyword = params.get("keyword") || "";

  return (
    <main className="main-content search-page">
      <Posts searchType={type} keyword={keyword} />
      <SideBar />   {/*  사이드바 추가 */}
    </main>
  );
}
