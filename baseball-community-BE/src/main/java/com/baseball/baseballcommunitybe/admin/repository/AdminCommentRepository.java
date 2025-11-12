package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminComment;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminCommentRepository extends JpaRepository<AdminComment, Long>  {

    // 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId")
    int countCommentsByUserId(@Param("userId") Long userId);
    // 🔹 댓글 작성자 조회 (경고 / 정지 시 필요)
    @Query("SELECT c.user.id FROM Comment c WHERE c.id = :commentId")
    Long findAuthorIdByCommentId(@Param("commentId") Long commentId);


    // 🔹 댓글 숨김 처리 (soft delete)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Comment c SET c.hidden = true WHERE c.id = :commentId")
    int hideComment(@Param("commentId") Long commentId);


    // 🔹 댓글 완전 삭제 (soft delete 전용)
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.id = :commentId")
    int softDelete(@Param("commentId") Long commentId);

}
