package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 최신글 조회
    @Query(
            value = """
            SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
                p.id, p.title, p.content, u.nickname,
                p.createdAt, p.updatedAt,
                s.commentCount, s.likeCount, s.viewCount
            )
            FROM Post p
            JOIN p.user u
            JOIN PostStatus s ON s.postId = p.id
            ORDER BY p.createdAt DESC
            """,
            countQuery = """
            SELECT COUNT(p.id)
            FROM Post p
            """
    )
    Page<PostResponseDto> findAllWithStatus(Pageable pageable);

    // 팀별 최신글 조회
    @Query(
            value = """
            SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
                p.id, p.title, p.content, u.nickname,
                p.createdAt, p.updatedAt,
                s.commentCount, s.likeCount, s.viewCount
            )
            FROM Post p
            JOIN p.user u
            JOIN PostStatus s ON s.postId = p.id
            WHERE p.teamId = :teamId
            ORDER BY p.createdAt DESC
            """,
            countQuery = """
            SELECT COUNT(p.id)
            FROM Post p
            WHERE p.teamId = :teamId
            """
    )
    Page<PostResponseDto> findByTeamIdWithStatus(@Param("teamId") Integer teamId, Pageable pageable);

    // 특정 유저의 글 조회
    @Query(
            value = """
            SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
                p.id, p.title, p.content, u.nickname,
                p.createdAt, p.updatedAt,
                s.commentCount, s.likeCount, s.viewCount
            )
            FROM Post p
            JOIN p.user u
            JOIN PostStatus s ON s.postId = p.id
            WHERE u.id = :userId
            ORDER BY p.createdAt DESC
            """,
            countQuery = """
            SELECT COUNT(p.id)
            FROM Post p
            WHERE p.user.id = :userId
            """
    )
    Page<PostResponseDto> findByUserIdWithStatus(@Param("userId") Long userId, Pageable pageable);

    // 검색
    @Query(
            value = """
            SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
                p.id, p.title, p.content, u.nickname,
                p.createdAt, p.updatedAt,
                s.commentCount, s.likeCount, s.viewCount
            )
            FROM Post p
            JOIN p.user u
            JOIN PostStatus s ON s.postId = p.id
            WHERE (:type = 'title' AND p.title LIKE CONCAT(:keyword, '%'))
               OR (:type = 'nickname' AND u.nickname LIKE CONCAT(:keyword, '%'))
               OR (:type = 'all' AND (
                      p.title LIKE CONCAT(:keyword, '%')
                   OR u.nickname LIKE CONCAT(:keyword, '%')
               ))
            ORDER BY p.createdAt DESC
            """,
            countQuery = """
            SELECT COUNT(p.id)
            FROM Post p
            JOIN p.user u
            WHERE (:type = 'title' AND p.title LIKE CONCAT(:keyword, '%'))
               OR (:type = 'nickname' AND u.nickname LIKE CONCAT(:keyword, '%'))
               OR (:type = 'all' AND (
                      p.title LIKE CONCAT(:keyword, '%')
                   OR u.nickname LIKE CONCAT(:keyword, '%')
               ))
            """
    )
    Page<PostResponseDto> searchPosts(
            @Param("type") String type,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}
