package com.baseball.baseballcommunitybe.like.repository;

import com.baseball.baseballcommunitybe.like.entity.CommentLike;
import com.baseball.baseballcommunitybe.like.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
    Optional<CommentLike> findByIdCommentIdAndIdUserId(Long commentId, Long userId);

    Long countByIdCommentIdAndType(Long commentId, CommentLike.Type type);

    void deleteByIdCommentIdAndIdUserId(Long commentId, Long userId);

    boolean existsByIdCommentIdAndIdUserId(Long commentId, Long userId);
}
