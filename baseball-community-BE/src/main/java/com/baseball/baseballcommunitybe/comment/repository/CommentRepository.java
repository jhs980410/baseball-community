package com.baseball.baseballcommunitybe.comment.repository;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPostId(Long postId);
}
