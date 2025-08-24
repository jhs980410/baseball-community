import React from "react";
import Header from "../../components/Header";
import SideBar from "../../components/SideBar";
import Posts from "../Posts/Posts";
import "../../styles/Home.css";
import NavBar from "../../components/NavBar";
export default function Home() {
  return (
    <div className="home-container">
      <Header />
      <NavBar />
      <main className="main-content">
        <Posts />
        <SideBar />
      </main>
    </div>
  );
}
