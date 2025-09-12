package com.baseball.baseballcommunitybe.comment.repository;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);

    List<Comment> findByPostId(Long postId);

    // 특정 게시글 댓글 목록 (유저 닉네임까지 페치조인)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdWithUser(@Param("postId") Long postId);

    // 유저 댓글 페이징 (최신순)
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    Page<Comment> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    // 중복 방지: 기본 findByUserId(Pageable)도 가능
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    // 게시글별 댓글 개수
    Long countByPostId(Long postId);
}
