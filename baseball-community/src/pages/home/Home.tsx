// src/pages/Home/Home.tsx
import React from "react";
import Posts from "../Posts/Posts";
import SideBar from "../../components/SideBar";
import "../../styles/Home.css";

export default function Home() {
  return (
    <>
     <main className="main-content home"> <Posts /> <SideBar /> </main>
    </>
  );
}
