package com.baseball.baseballcommunitybe.comment.controller;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.service.CommentService;
import com.baseball.baseballcommunitybe.common.aop.CheckSuspended;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 특정 게시글 댓글 + 대댓글 조회
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPost(postId));
    }

    /**
     * 마이페이지: 내 댓글 조회 (JWT 기반)
     */
    @GetMapping("/me")
    public ResponseEntity<Page<CommentResponseDto>> getMyComments(
            HttpServletRequest request,
            Pageable pageable
    ) {
        Long currentUserId = extractUserId(request);
        return ResponseEntity.ok(
                commentService.findByUserIdOrderByCreatedAtDesc(currentUserId, pageable)
        );
    }

    /**
     * 댓글/대댓글 작성 (parentId 유무로 구분)
     */
    @CheckSuspended
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CommentRequestDto dto,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(commentService.create(dto, request));
    }

    /**
     * 댓글 수정 (작성자만 가능)
     */
    @CheckSuspended
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody String newContent,
            HttpServletRequest request
    ) {
        Long currentUserId = extractUserId(request);
        CommentResponseDto updated = commentService.update(id, newContent, currentUserId);
        return ResponseEntity.ok(updated);
    }

    /**
     * 댓글 삭제 (작성자 또는 관리자만)
     */
    @CheckSuspended
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long currentUserId = extractUserId(request);
        boolean isAdmin = extractIsAdmin(request); //  관리자 여부 체크
        commentService.delete(id, currentUserId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    // ------------------ 내부 유틸 ------------------
    private Long extractUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return (token != null) ? jwtTokenProvider.getUserIdFromToken(token) : null;
    }

    private boolean extractIsAdmin(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return token != null && jwtTokenProvider.getRoleFromToken(token).equals("ADMIN");
    }
}
