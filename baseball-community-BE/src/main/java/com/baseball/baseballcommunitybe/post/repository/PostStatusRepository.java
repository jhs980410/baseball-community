package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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


    @Query("SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(" +
            "p.id, p.title, p.content, u.nickname, p.createdAt, p.updatedAt, " +
            "s.commentCount, s.likeCount, s.viewCount) " +
            "FROM Post p " +
            "JOIN p.user u " +
            "JOIN PostStatus s ON p.id = s.postId " +
            "WHERE (:teamId IS NULL OR p.teamId = :teamId) " +
            "ORDER BY s.score DESC")
    Page<PostResponseDto> findHotPosts(@Param("teamId") Integer teamId, Pageable pageable);

    // PostStatusRepository
    @Query("SELECT ps FROM PostStatus ps JOIN FETCH ps.post p")
    Page<PostStatus> findAllWithPost(Pageable pageable);

    @Query("SELECT ps FROM PostStatus ps JOIN FETCH ps.post p WHERE ps.postId IN :ids")
    List<PostStatus> findAllByPostIdIn(@Param("ids") List<Long> ids);


    @Query("SELECT ps FROM PostStatus ps JOIN FETCH ps.post p " +
            "WHERE p.createdAt >= :since")
    Page<PostStatus> findRecent(@Param("since") LocalDateTime since, Pageable pageable);

}
