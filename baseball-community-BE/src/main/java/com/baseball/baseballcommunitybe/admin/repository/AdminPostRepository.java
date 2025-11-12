package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long>, JpaSpecificationExecutor<AdminPost> {

    // ---------------------------------------------------
    // 기본 통계 / 조회
    // ---------------------------------------------------

    long count();

    List<AdminPost> findTop5ByOrderByStatus_LikeCountDesc();

    /**
     * 기존 사용자 게시글 수 조회 (유지)
     */
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
    int countPostsByUserId(@Param("userId") Long userId);

    /**
     * 특정 유저 게시글 수 (통계용)
     */
    @Query("SELECT COUNT(p) FROM AdminPost p WHERE p.userId = :userId")
    long countByUserId(@Param("userId") Long userId);


    // ---------------------------------------------------
    // 게시글 + 상태정보(PostStatus) 함께 조회 (N+1 방지)
    // ---------------------------------------------------

    /**
     * Formula 포함 일반 엔티티 조회용 (AdminPost 엔티티 그대로 반환)
     * → AdminPost#getIsHidden()에서 Boolean 변환 처리됨
     */
    @EntityGraph(attributePaths = {"status"})
    @Query("SELECT p FROM AdminPost p ORDER BY p.id DESC")
    Page<AdminPost> findAllWithStatus(Pageable pageable);

    /**
     * DTO 기반 조회 (Formula 미적용 환경에서도 확실하게 isHidden 반환)
     */
    @Query("""
SELECT new com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto(
    p.id,
    p.title,
    p.user.id,
    p.teamId,
    COALESCE(s.commentCount, 0L),
    COALESCE(s.likeCount,    0L),
    COALESCE(s.viewCount,    0L),
    CASE WHEN p.isHidden = true THEN true ELSE false END
)
FROM Post p
LEFT JOIN AdminPostStatus s ON s.postId = p.id
ORDER BY p.id DESC
""")
    Page<AdminPostDto> findAllWithStatusAndHidden(Pageable pageable);


    // ---------------------------------------------------
    // 관리자용 게시글 제어 (soft delete / restore)
    // ---------------------------------------------------

    /**
     * 게시글 숨김 처리 (soft delete)
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.isHidden = true WHERE p.id = :postId")
    int hidePost(@Param("postId") Long postId);

    /**
     * 게시글 복구 처리
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.isHidden = false WHERE p.id = :postId")
    int restorePost(@Param("postId") Long postId);


    // ---------------------------------------------------
    // 관리자용 게시글 상세조회
    // ---------------------------------------------------

    /**
     * 관리자 상세조회: is_hidden 포함 (AdminPost는 그대로, DB 컬럼 직접 select)
     */
    @Query("""
    SELECT new com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDetailDto(
        p.id,
        p.title,
        p.content,
        p.user.id,
        p.teamId,
        COALESCE(s.commentCount, 0L),
        COALESCE(s.likeCount,    0L),
        COALESCE(s.viewCount,    0L),
        COALESCE(s.reportCount,  0L),
        COALESCE(s.containsBannedWord, false),
        COALESCE(s.flagged, false),
        s.lastFlagReason,
        p.isHidden
    )
    FROM Post p
    LEFT JOIN AdminPostStatus s ON s.postId = p.id
    WHERE p.id = :postId
    """)
    Optional<AdminPostDetailDto> findDetailWithHidden(@Param("postId") Long postId);
    /**
     * 신고 대상 게시글 작성자 ID 조회
     */
    @Query("SELECT p.user.id FROM Post p WHERE p.id = :postId")
    Long findAuthorIdByPostId(@Param("postId") Long postId);

    /**
     * 게시글 숨김 (관리자 조치)
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.isHidden = TRUE WHERE p.id = :postId")
    void adminHidePost(@Param("postId") Long postId);

    /**
     * 게시글 삭제 (soft delete)
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.isHidden = TRUE WHERE p.id = :postId")
    void adminSoftDelete(@Param("postId") Long postId);
}
