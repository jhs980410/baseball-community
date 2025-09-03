package com.baseball.baseballcommunitybe.like.controller;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 특정 유저가 좋아요한 글 조회 (마이페이지에서 활용)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeResponseDto>> getLikesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.findByUser(userId));
    }

    // 좋아요 추가
    @PostMapping("/{postId}/user/{userId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @DeleteMapping("/{postId}/user/{userId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 좋아요 개수 조회
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }
}
