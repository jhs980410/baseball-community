package com.baseball.baseballcommunitybe.post.repository;

import com.baseball.baseballcommunitybe.post.entity.PostEditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostEditHistoryRepository extends JpaRepository<PostEditHistory, Long> {
    List<PostEditHistory> findByPostIdOrderByEditedAtDesc(Long postId);

    boolean existsByPostId(Long postId);
}
