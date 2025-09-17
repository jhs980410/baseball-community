package com.baseball.baseballcommunitybe.like.controller;

import com.baseball.baseballcommunitybe.like.dto.CommentLikeResponseDto;
import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.service.CommentLikeService;
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

    private final LikeService likeService;              // 게시글 좋아요
    private final CommentLikeService commentLikeService; // 댓글 좋아요

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
     * 게시글 좋아요 토글
     */
    @PostMapping("/posts/{postId}/toggle")
    public ResponseEntity<LikeResponseDto> togglePostLike(
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
    public ResponseEntity<Long> countPostLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }

    // ================= 댓글 좋아요 ================= //

// ================= 댓글 좋아요 ================= //

    /**
     * 댓글 좋아요 추가
     */
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<CommentLikeResponseDto> likeComment(
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        CommentLikeResponseDto response = commentLikeService.like(commentId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 좋아요 취소
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentLikeResponseDto> unlikeComment(
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        CommentLikeResponseDto response = commentLikeService.unlike(commentId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 좋아요 개수 조회
     */
    @GetMapping("/comments/{commentId}/count")
    public ResponseEntity<Long> countCommentLikes(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentLikeService.countLikes(commentId));
    }
}
