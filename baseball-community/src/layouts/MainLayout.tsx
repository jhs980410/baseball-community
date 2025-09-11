// src/layouts/MainLayout.tsx
import React from "react";
import Header from "../components/Header/Header";
import NavBar from "../components/NavBar/NavBar";
import { Outlet } from "react-router-dom";

export default function MainLayout() {
  return (
    <div className="home-container">
      <Header />
      <NavBar />
      {/* 페이지별로 바뀌는 부분 */}
      <main className="main-content">
        <Outlet />
        
      </main>
    </div>
  );
}
