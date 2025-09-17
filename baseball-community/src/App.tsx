// src/App.tsx
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import Home from "./pages/home/Home";
import Register from "./pages/Login/Register";
import PostDetail from "./pages/Posts/PostsDetail";
import Mypage from "./pages/user/Mypage";
import TeamPage from "./pages/board/TeamPage";
import { AuthProvider } from "./contexts/AuthContext";
import PostCreate from "./pages/Posts/PostCreate";
import PostEdit from "./pages/Posts/PostEdit";
import SearchPage from "./pages/board/SearchPage";

function App() {
  return (
    <AuthProvider>
    <BrowserRouter>
      <Routes>
        {/* 공통 레이아웃 */}
        <Route element={<MainLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/posts/:postId" element={<PostDetail />} />
          <Route path="/posts/create" element={<PostCreate />} />
          <Route path="/posts/:postId/edit" element={<PostEdit />} />
          <Route path="/mypage" element={<Mypage />} />
          <Route path="/teams/:id" element={<TeamPage />} />
          <Route path="/search" element={<SearchPage />} />
    
        </Route>  
      </Routes>
    </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
