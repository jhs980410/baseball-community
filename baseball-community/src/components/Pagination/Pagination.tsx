import React from "react";
import "./Pagination.css";

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function Pagination({ currentPage, totalPages, onPageChange }: PaginationProps) {
  const pageGroupSize = 10; // 한 번에 보여줄 페이지 수
  const currentGroup = Math.floor(currentPage / pageGroupSize);
  const startPage = currentGroup * pageGroupSize;
  const endPage = Math.min(startPage + pageGroupSize - 1, totalPages - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(
      <button
        key={i}
        className={i === currentPage ? "active" : ""}
        onClick={() => onPageChange(i)}
      >
        {i + 1}
      </button>
    );
  }

  return (
    <div className="pagination">
      <button
        disabled={currentGroup === 0}
        onClick={() => onPageChange(startPage - 1)}
      >
        이전
      </button>

      {pages}

      <button
        disabled={endPage >= totalPages - 1}
        onClick={() => onPageChange(endPage + 1)}
      >
        다음
      </button>
    </div>
  );
}
