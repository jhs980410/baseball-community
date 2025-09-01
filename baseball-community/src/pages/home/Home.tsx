// src/pages/Home/Home.tsx
import React from "react";
import Posts from "../Posts/Posts";
import SideBar from "../../components/SideBar/SideBar";
import "./Home.css";

export default function Home() {
  return (
    <>
     <main className="main-content home">  <Posts teamId="all" />
      <SideBar /> </main>
    </>
  );
}
