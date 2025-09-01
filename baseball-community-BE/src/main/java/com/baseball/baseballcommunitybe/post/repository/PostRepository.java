package com.baseball.baseballcommunitybe.post.repository;


import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>  {

    // 전체 최신글 조회 (최신순)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 팀별 최신글 조회 (최신순)
    Page<Post> findByTeamIdOrderByCreatedAtDesc(Integer teamId, Pageable pageable);

    // 특정 유저의 글 조회 (최신순)
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

}
