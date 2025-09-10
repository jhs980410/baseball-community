package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 최신글 조회 (댓글 개수 포함 - 서브쿼리 방식)
    @Query("""
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            p.createdAt, p.updatedAt,
            (SELECT COUNT(c.id) as long FROM Comment c WHERE c.post.id = p.id)
        )
        FROM Post p
        JOIN p.user u
        ORDER BY p.createdAt DESC
        """)
    Page<PostResponseDto> findAllWithCommentCount(Pageable pageable);

    // 팀별 최신글 조회 (댓글 개수 포함 - 서브쿼리 방식)
    @Query("""
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            p.createdAt, p.updatedAt,
            (SELECT COUNT(c.id) as long FROM Comment c WHERE c.post.id = p.id)
        )
        FROM Post p
        JOIN p.user u
        WHERE p.teamId = :teamId
        ORDER BY p.createdAt DESC
        """)
    Page<PostResponseDto> findByTeamIdWithCommentCount(@Param("teamId") Integer teamId, Pageable pageable);

    // 특정 유저의 글 조회 (댓글 개수 포함 - 서브쿼리 방식)
    @Query("""
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            p.createdAt, p.updatedAt,
            (SELECT COUNT(c.id) as long FROM Comment c WHERE c.post.id = p.id)
        )
        FROM Post p
        JOIN p.user u
        WHERE u.id = :userId
        ORDER BY p.createdAt DESC
        """)
    Page<PostResponseDto> findByUserIdWithCommentCount(@Param("userId") Long userId, Pageable pageable);

    //검색
    @Query("""
    SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
        p.id, p.title, p.content, u.nickname,
        p.createdAt, p.updatedAt,
        (SELECT COUNT(c.id) as long FROM Comment c WHERE c.post.id = p.id)
    )
    FROM Post p
    JOIN p.user u
    WHERE (:type = 'title' AND p.title LIKE %:keyword%)
       OR (:type = 'nickname' AND u.nickname LIKE %:keyword%)
       OR (:type = 'all' AND (p.title LIKE %:keyword% OR u.nickname LIKE %:keyword%))
    ORDER BY p.createdAt DESC
    """)
    Page<PostResponseDto> searchPosts(
            @Param("type") String type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
