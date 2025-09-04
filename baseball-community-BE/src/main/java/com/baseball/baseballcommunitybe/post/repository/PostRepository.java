package com.baseball.baseballcommunitybe.post.repository;


import com.baseball.baseballcommunitybe.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>  {
    // 전체 최신글 조회 (User 페치조인)
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    // 팀별 최신글 조회 (User 페치조인)
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.teamId = :teamId ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.teamId = :teamId")
    Page<Post> findByTeamIdWithUser(@Param("teamId") Integer teamId, Pageable pageable);

    // 특정 유저의 글 조회 (User 페치조인)
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id = :userId ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
    Page<Post> findByUserIdWithUser(@Param("userId") Long userId, Pageable pageable);
}
