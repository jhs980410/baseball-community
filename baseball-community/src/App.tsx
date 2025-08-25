// src/App.tsx
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import Home from "./pages/home/Home";
import Register from "./pages/Login/Register";
import PostDetail from "./pages/Posts/PostsDetail";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 공통 레이아웃 */}
        <Route element={<MainLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/posts/:id" element={<PostDetail />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
