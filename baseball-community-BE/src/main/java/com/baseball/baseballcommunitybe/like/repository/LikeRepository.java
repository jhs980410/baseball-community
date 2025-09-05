package com.baseball.baseballcommunitybe.like.repository;

import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findByUser_Id(Long userId);   //  올바른 메서드
    long countByPost_Id(Long postId);
    boolean existsById(LikeId id);
    Page<Like> findByUser_Id(Long userId, Pageable pageable);
}
