package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminPost;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long> {
    long count();

    List<AdminPost> findTop5ByOrderByLikeCountDesc();
}
