import { type Notice } from "../../types/notice";

export const mockNotices: Notice[] = [
  {
    id: 1,
    title: "시스템 점검 안내",
    content: "9월 20일 오전 2시~5시까지 점검이 진행됩니다.",
    is_pinned: true,
    created_at: "2025-09-10 09:00:00",
    updated_at: "2025-09-10 09:00:00",
  },
  {
    id: 2,
    title: "이벤트 안내",
    content: "야구 커뮤니티 가을 이벤트에 참여하세요!",
    is_pinned: false,
    created_at: "2025-09-11 10:20:00",
    updated_at: "2025-09-11 10:20:00",
  },
  {
    id: 3,
    title: "운영 정책 변경",
    content: "게시글 신고 처리 규정이 일부 변경됩니다.",
    is_pinned: false,
    created_at: "2025-09-12 14:30:00",
    updated_at: "2025-09-12 14:30:00",
  },
  {
    id: 4,
    title: "신규 기능 추가",
    content: "인기글 순위 집계 기능이 추가되었습니다.",
    is_pinned: true,
    created_at: "2025-09-13 09:10:00",
    updated_at: "2025-09-13 09:10:00",
  },
  {
    id: 5,
    title: "회원 정지 안내",
    content: "운영 방침 위반 회원 2명이 정지되었습니다.",
    is_pinned: false,
    created_at: "2025-09-13 16:00:00",
    updated_at: "2025-09-13 16:00:00",
  },
  // 6 ~ 20
  ...Array.from({ length: 15 }, (_, i) => ({
    id: i + 6,
    title: `공지사항 ${i + 6}`,
    content: `테스트용 공지사항 내용입니다 (${i + 6}).`,
    is_pinned: i % 5 === 0,
    created_at: `2025-09-${14 + i} 08:00:00`,
    updated_at: `2025-09-${14 + i} 08:00:00`,
  })),
];
