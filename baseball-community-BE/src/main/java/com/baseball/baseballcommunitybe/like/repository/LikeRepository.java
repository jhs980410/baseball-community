package com.baseball.baseballcommunitybe.like.repository;

import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    // 특정 유저가 누른 좋아요 (리스트)
    List<Like> findByUser_Id(Long userId);

    // 특정 유저가 누른 좋아요 (페이징)
    Page<Like> findByUser_Id(Long userId, Pageable pageable);

    // 게시글별 좋아요 개수
    long countByPost_Id(Long postId);

    // 특정 유저가 특정 게시글에 좋아요 했는지 여부
    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId);

    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);

}
