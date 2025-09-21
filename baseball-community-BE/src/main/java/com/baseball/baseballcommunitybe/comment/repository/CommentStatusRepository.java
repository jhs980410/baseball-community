package com.baseball.baseballcommunitybe.comment.repository;

import com.baseball.baseballcommunitybe.comment.entity.CommentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentStatusRepository extends JpaRepository<CommentStatus, Long> {

    // 좋아요 +1
    @Modifying
    @Transactional
    @Query("UPDATE CommentStatus cs SET cs.likeCount = cs.likeCount + 1, cs.lastUpdated = CURRENT_TIMESTAMP WHERE cs.commentId = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    // 좋아요 -1
    @Modifying
    @Transactional
    @Query("UPDATE CommentStatus cs SET cs.likeCount = cs.likeCount - 1, cs.lastUpdated = CURRENT_TIMESTAMP WHERE cs.commentId = :commentId")
    void decrementLikeCount(@Param("commentId") Long commentId);
}
