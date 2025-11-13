package com.baseball.baseballcommunitybe.notice.repository;

import com.baseball.baseballcommunitybe.notice.dto.NoticeListResponseDto;
import com.baseball.baseballcommunitybe.notice.dto.NoticeResponseDto;
import com.baseball.baseballcommunitybe.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // DTO 반환 → NoticeListResponseDto 로 받아야 함
    @Query("""
    SELECT new com.baseball.baseballcommunitybe.notice.dto.NoticeListResponseDto(
        n.id,
        n.title,
        n.createdAt,
        n.pinned,
        0, 0, 0
    )
    FROM Notice n
    WHERE n.pinned = true
    ORDER BY n.createdAt DESC
""")
    List<NoticeListResponseDto> findTopPinned();
    // 메서드 이름 기반 쿼리도 엔티티 필드명 그대로 사용
    List<Notice> findByPinnedTrueOrderByCreatedAtDesc();


    //  전체 공지 리스트 (새 공지가 앞에 오게)
    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);


    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.notice.dto.NoticeListResponseDto(
            n.id, n.title,
            n.createdAt, n.pinned,
            0, 0, 0
        )
        FROM Notice n
        ORDER BY n.createdAt DESC
        """,
            countQuery = """
        SELECT COUNT(n.id)
        FROM Notice n
        """
    )
    Page<NoticeListResponseDto> findAllNoticeList(Pageable pageable);


}
