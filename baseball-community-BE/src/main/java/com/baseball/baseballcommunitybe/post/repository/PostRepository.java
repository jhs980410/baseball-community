package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 최신글 조회 (댓글 개수 포함)
    @Query("SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(" +
            "p.id, p.title, p.content, p.user.nickname, " +
            "CAST(p.createdAt AS string), CAST(p.updatedAt AS string), COUNT(c.id)) " +
            "FROM Post p LEFT JOIN p.comments c " +
            "GROUP BY p.id, p.title, p.content, p.user.nickname, p.createdAt, p.updatedAt " +
            "ORDER BY p.createdAt DESC")
    Page<PostResponseDto> findAllWithCommentCount(Pageable pageable);

    // 팀별 최신글 조회 (댓글 개수 포함)
    @Query("SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(" +
            "p.id, p.title, p.content, p.user.nickname, " +
            "CAST(p.createdAt AS string), CAST(p.updatedAt AS string), COUNT(c.id)) " +
            "FROM Post p LEFT JOIN p.comments c " +
            "WHERE p.teamId = :teamId " +
            "GROUP BY p.id, p.title, p.content, p.user.nickname, p.createdAt, p.updatedAt " +
            "ORDER BY p.createdAt DESC")
    Page<PostResponseDto> findByTeamIdWithCommentCount(@Param("teamId") Integer teamId, Pageable pageable);

    // 특정 유저의 글 조회 (댓글 개수 포함)
    @Query("SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(" +
            "p.id, p.title, p.content, p.user.nickname, " +
            "CAST(p.createdAt AS string), CAST(p.updatedAt AS string), COUNT(c.id)) " +
            "FROM Post p LEFT JOIN p.comments c " +
            "WHERE p.user.id = :userId " +
            "GROUP BY p.id, p.title, p.content, p.user.nickname, p.createdAt, p.updatedAt " +
            "ORDER BY p.createdAt DESC")
    Page<PostResponseDto> findByUserIdWithCommentCount(@Param("userId") Long userId, Pageable pageable);
}
