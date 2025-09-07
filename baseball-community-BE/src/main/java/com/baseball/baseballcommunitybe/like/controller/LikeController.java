package com.baseball.baseballcommunitybe.like.controller;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 특정 유저가 좋아요한 글 조회 (마이페이지)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<LikeResponseDto>> getLikesByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(likeService.findByUser(userId, pageable));
    }

    /**
     * 좋아요 토글
     */
    @PostMapping("/{postId}/user/{userId}/toggle")
    public ResponseEntity<LikeResponseDto> toggleLike(
            @PathVariable Long postId,
            @PathVariable Long userId
    ) {
        LikeResponseDto response = likeService.toggleLike(postId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 좋아요 개수 조회
     */
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }
}
