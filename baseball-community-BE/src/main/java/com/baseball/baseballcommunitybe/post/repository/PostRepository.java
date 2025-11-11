package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    /** ✅ 전체 최신글 조회 (숨김 제외) */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON s.postId = p.id
        WHERE p.isHidden = false
        ORDER BY p.createdAt DESC
        """,
            countQuery = """
        SELECT COUNT(p.id)
        FROM Post p
        WHERE p.isHidden = false
        """
    )
    Page<PostResponseDto> findAllWithStatus(Pageable pageable);

    /**  팀별 최신글 조회 */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON s.postId = p.id
        WHERE p.teamId = :teamId AND p.isHidden = false
        ORDER BY p.createdAt DESC
        """,
            countQuery = """
        SELECT COUNT(p.id)
        FROM Post p
        WHERE p.teamId = :teamId AND p.isHidden = false
        """
    )
    Page<PostResponseDto> findByTeamIdWithStatus(@Param("teamId") Integer teamId, Pageable pageable);

    /**  특정 유저의 글 조회 */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON s.postId = p.id
        WHERE u.id = :userId AND p.isHidden = false
        ORDER BY p.createdAt DESC
        """,
            countQuery = """
        SELECT COUNT(p.id)
        FROM Post p
        WHERE p.user.id = :userId AND p.isHidden = false
        """
    )
    Page<PostResponseDto> findByUserIdWithStatus(@Param("userId") Long userId, Pageable pageable);

    /**  검색 (제목, 닉네임, 통합검색) */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON s.postId = p.id
        WHERE p.isHidden = false AND (
              (:type = 'title' AND p.title LIKE CONCAT(:keyword, '%'))
           OR (:type = 'nickname' AND u.nickname LIKE CONCAT(:keyword, '%'))
           OR (:type = 'all' AND (
                  p.title LIKE CONCAT(:keyword, '%')
               OR u.nickname LIKE CONCAT(:keyword, '%')
           ))
        )
        ORDER BY p.createdAt DESC
        """,
            countQuery = """
        SELECT COUNT(p.id)
        FROM Post p
        JOIN p.user u
        WHERE p.isHidden = false AND (
              (:type = 'title' AND p.title LIKE CONCAT(:keyword, '%'))
           OR (:type = 'nickname' AND u.nickname LIKE CONCAT(:keyword, '%'))
           OR (:type = 'all' AND (
                  p.title LIKE CONCAT(:keyword, '%')
               OR u.nickname LIKE CONCAT(:keyword, '%')
           ))
        )
        """
    )
    Page<PostResponseDto> searchVisiblePosts(
            @Param("type") String type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**  팀별 인기글 조회 */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON p.id = s.postId
        WHERE p.teamId = :teamId AND p.isHidden = false
        ORDER BY s.score DESC
        """
    )
    Page<PostResponseDto> findHotVisiblePostsByTeam(@Param("teamId") Integer teamId, Pageable pageable);

    /**  전체 인기글 조회 */
    @Query(
            value = """
        SELECT new com.baseball.baseballcommunitybe.post.dto.PostResponseDto(
            p.id, p.title, p.content, u.nickname,
            u.id, p.teamId,
            p.createdAt, p.updatedAt,
            s.commentCount, s.likeCount, s.viewCount,
            p.isHidden
        )
        FROM Post p
        JOIN p.user u
        JOIN PostStatus s ON p.id = s.postId
        WHERE p.isHidden = false
        ORDER BY s.score DESC
        """
    )
    Page<PostResponseDto> findHotVisiblePosts(Pageable pageable);
}
