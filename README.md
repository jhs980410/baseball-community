# ⚾ Baseball Community

React · Spring Boot · MySQL · Redis 기반 **야구 커뮤니티 플랫폼**

<div align="center"> 

  <strong>실시간 게시판, 댓글, 좋아요, 조회수, 인기글 캐싱, 관리자 시스템까지 갖춘 풀스택 커뮤니티 서비스</strong>
</div>

---

## 📢 프로젝트 소개 (Project Overview)

Baseball Community는  
야구 팬들이 자유롭게 의견을 공유하고, 팀별 게시판에서 소통하며,  
관리자가 신고·운영을 수행할 수 있는 **Full-Stack 커뮤니티 플랫폼**입니다.

- 게시글 / 댓글 / 좋아요 / 조회수 기능 제공  
- Redis 기반 인기글(Hot Posts) 캐싱  
- 사용자/관리자 Front 분리  
- SUPER_ADMIN / ADMIN / USER 권한 구조  
- Docker · Nginx · GHCR · GitHub Actions 기반 자동 배포

---

## 📆 개발 기간
- 2025.09 ~ 2025.11 (지속 업데이트 중)

## 😎 팀 구성
- **개인 프로젝트 (Full-Stack / Infra 포함 전부 직접 구현)**

---

## ⚙ 개발 환경

- **Frontend (User/Admin)** : React · Vite · TypeScript · Axios  
- **Backend** : Spring Boot 3.5.4 · Spring Security 6 · JWT  
- **Database** : MySQL 8  
- **Cache** : Redis (Top 200 인기글 캐싱)  
- **Infra** : AWS EC2 · Docker · Nginx · GHCR · GitHub Actions(CI/CD)  
- **OS / Tools** : Ubuntu 22.04 · IntelliJ IDEA · VS Code  

---

## 📌 주요 기능

- 로그인 및 회원가입
- 게시글 / 댓글 / 좋아요 / 조회수 / 팀별 게시판
- 인기글 Top 200 캐싱 (Redis)
- 마이페이지(내 글 / 내 댓글 / 좋아요한 글)
- 관리자 페이지 분리(Admin Front)
- SUPER_ADMIN / ADMIN / USER 권한 계층 구조
- GitHub Actions 기반 자동 배포 (Docker · GHCR)

---

# 🧩 시스템 아키텍처

## Frontend (User/Admin)

- React + Vite + TypeScript
- Axios 기반 REST API 통신
- User Front / Admin Front **도메인 분리 운영**

---

## Backend (Spring Boot 3.x)

- JWT 기반 인증
- Spring Security 6 (Role 기반 접근 제어)
- API 구조:
  - /api/auth — 인증
  - /api/posts — 게시판
  - /api/comments — 댓글
  - /api/likes — 좋아요
  - /api/reports — 신고
  - /api/admin/** — 관리자 기능
  - /api/super/** — 슈퍼관리자 기능

---

## Database & Cache Layer

### MySQL (영속 데이터)
- posts  
- comments  
- likes  
- users  
- reports  
- notices  
- status 테이블(post/comment)  
- admin_logs  

### Redis (캐시)
- 인기글 Top 200  
- 조회수  
- Daily Stats  

---

## Infra (배포 환경)

- AWS EC2 단일 서버 운영
- Nginx Reverse Proxy + HTTPS (Certbot)
- Docker 컨테이너 기반
- GitHub Actions + GHCR → EC2 자동 배포 파이프라인 구축

---

## 🗄 DB 구조 (요약 ERD)

**주요 엔티티 목록**

- Users, UserSuspensions  
- Posts, PostStatus, PostEditHistory, PostImages  
- Comments, CommentStatus, CommentEditHistory  
- Likes, CommentLikes  
- Reports, BanWords  
- Notices  
- AdminLogs  
- DailyStats, DailyTopPosts, DailyTopComments  

**관계 요약**

- Users 1 ─ N Posts 1 ─ N Comments  
- Users 1 ─ N Likes  
- Users 1 ─ N Reports  
- Posts 1 ─ 1 PostStatus  
- Comments 1 ─ 1 CommentStatus  
- Admin 1 ─ N AdminLogs  


<img width="814" height="628" alt="스키마 구조" src="https://github.com/user-attachments/assets/164d04a6-2db5-407f-9eda-0f0b2efe3504" />

---

# 📚 API 전체 문서 (Full API Documentation)

## 1. Auth API (/api/auth)
- POST /signup  
- POST /login  
- POST /logout  
- POST /refresh  
- POST /verify-password  
- GET /me  

## 2. User API (/api/users)
- GET /me  
- PUT /me  
- DELETE /me  
- GET /{id}  
- GET /check-email  
- GET /check-nickname  
- GET /me/suspend-info  

## 3. Post API (/api/posts)
### 조회
- GET /  
- GET /{postId}  
- GET /teams/{teamId}  
- GET /me  
- GET /users/{userId}  
### CRUD
- POST /  
- PUT /{postId}  
- DELETE /{postId}  
### 인기글
- GET /hot  
- GET /teams/{teamId}/hot  

## 4. Comment API (/api/comments)
- GET /posts/{postId}  
- GET /me  
- POST /  
- PUT /{id}  
- DELETE /{id}  

## 5. Like API (/api/likes)
### 게시글
- POST /{postId}/toggle  
- GET /posts/{postId}/count  
### 댓글  
- POST /comments/{commentId}  
- DELETE /comments/{commentId}  
- GET /comments/{commentId}/count  
### 내가 누른 좋아요  
- GET /me  

## 6. Report API (/api/reports)
- POST /posts/{postId}  
- POST /comments/{commentId}  

## 7. Notice API (/api/notices)
- GET /{id}  
- GET /top  

## 8. Admin API (/api/admin/**)
### 인증
- POST /auth/login  
- DELETE /auth/logout  
- GET /auth/me  

### 게시글 관리
- GET /posts/{postId}  
- DELETE /posts/{postId}  
- PATCH /posts/{postId}/restore  
- PATCH /posts/{postId}/flag  
- GET /posts/count/{userId}  

### 신고 관리
- GET /reports/posts  
- GET /reports/comments  
- GET /reports/users  
- DELETE /reports/{id}  
- PATCH /reports/{id}/status  
- PATCH /reports/{reportId}/handle  

### 공지 관리
- GET /notices/{id}  
- PUT /notices/{id}  
- DELETE /notices/{id}  
- PATCH /notices/{id}/pin  

### 유저 관리
- GET /users/{id}  
- PATCH /users/{id}  
- PATCH /users/{id}/suspend  
- PATCH /users/{id}/unsuspend  

## 9. Super Admin API (/api/super/**)
- GET /admins/roles  
- PATCH /admins/{id}/role  
- DELETE /admins/{id}  
- POST /admins/transfer  

---


# ☁ 배포 구조 (CI/CD)

- GitHub Actions → GHCR 자동 Push  
- EC2(Server)에서 docker pull + PM2 실행  
- User/Admin Front는 Nginx 정적 제공  
- HTTPS(Certbot) 자동 갱신  
- Redis + MySQL + Spring Boot 단일 EC2 운영  

---

# 📌 TODO (개선 예정)

- 완전한 Soft Delete 도입 (users / posts / comments)
- AdminPostStatus 확장
- Redis 캐싱 구조 고도화
- Team 로고/에셋 관리
- Full-Text 검색 엔진 적용
- 관리자 로그 UI 개선
