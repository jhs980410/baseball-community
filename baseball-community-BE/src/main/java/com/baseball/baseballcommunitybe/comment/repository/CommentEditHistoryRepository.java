package com.baseball.baseballcommunitybe.comment.repository;

import com.baseball.baseballcommunitybe.comment.entity.CommentEditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentEditHistoryRepository extends JpaRepository<CommentEditHistory, Long> {
    boolean existsByComment_Id(Long commentId);

    Optional<CommentEditHistory> findTopByComment_IdOrderByEditedAtDesc(Long commentId);
}
