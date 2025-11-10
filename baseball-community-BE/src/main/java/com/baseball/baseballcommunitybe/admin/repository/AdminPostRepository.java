package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminPost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long> {
    long count();

    List<AdminPost> findTop5ByOrderByLikeCountDesc();

    // 특정유저 게시글 수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
    int countPostsByUserId(@Param("userId") Long userId);


}
