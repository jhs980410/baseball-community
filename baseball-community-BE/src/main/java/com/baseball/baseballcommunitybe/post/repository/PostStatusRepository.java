package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.entity.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {

    //  댓글 수 +1
    @Modifying
    @Transactional
    @Query("UPDATE PostStatus s SET s.commentCount = s.commentCount + 1, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.postId = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    //  댓글 수 -1
    @Modifying
    @Transactional
    @Query("UPDATE PostStatus s SET s.commentCount = s.commentCount - 1, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.postId = :postId")
    void decrementCommentCount(@Param("postId") Long postId);

    //  좋아요 +1
    @Modifying
    @Transactional
    @Query("UPDATE PostStatus s SET s.likeCount = s.likeCount + 1, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.postId = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    //  좋아요 -1
    @Modifying
    @Transactional
    @Query("UPDATE PostStatus s SET s.likeCount = s.likeCount - 1, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.postId = :postId")
    void decrementLikeCount(@Param("postId") Long postId);

    //  조회수 +1
    @Modifying
    @Transactional
    @Query("UPDATE PostStatus s SET s.viewCount = s.viewCount + 1, s.lastUpdated = CURRENT_TIMESTAMP WHERE s.postId = :postId")
    void incrementViewCount(@Param("postId") Long postId);
}
