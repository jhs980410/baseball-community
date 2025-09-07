package com.baseball.baseballcommunitybe.comment.controller;

import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 특정 게시글 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPost(postId));
    }

    // 특정 유저 댓글 조회 (마이페이지에서 활용 가능)
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(commentService.findByUser(userId, pageable));
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    // 댓글 삭제 (작성자 or 관리자만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId,        // 프론트에서 요청자 ID 전달
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        commentService.delete(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }


}
