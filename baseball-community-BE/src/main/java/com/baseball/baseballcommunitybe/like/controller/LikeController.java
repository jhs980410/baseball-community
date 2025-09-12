package com.baseball.baseballcommunitybe.like.controller;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
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
     * 내가 좋아요한 글 조회 (마이페이지)
     */
    @GetMapping("/me")
    public ResponseEntity<Page<LikeResponseDto>> getMyLikes(
            HttpServletRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(likeService.findMyLikes(request, pageable));
    }

    /**
     * 좋아요 토글
     */
    @PostMapping("/{postId}/toggle")
    public ResponseEntity<LikeResponseDto> toggleLike(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        LikeResponseDto response = likeService.toggleLike(postId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 좋아요 개수 조회
     */
    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }
}
