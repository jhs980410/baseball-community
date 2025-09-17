package com.baseball.baseballcommunitybe.comment.dto;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime date;
    private Long postId;       // 마이페이지 용도
    private String postTitle;  // 마이페이지 용도
    private Long parentId;     // 대댓글 구분
    private List<CommentResponseDto> children; // 게시글 상세용

    // 🔹 마이페이지 전용 팩토리 (children 없음)
    public static CommentResponseDto forUser(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getPost().getTitle(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                null // 마이페이지는 children 불필요
        );
    }

    // 🔹 게시글 상세 전용 팩토리 (트리 구조, children 재귀 변환)
    public static CommentResponseDto forPost(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getContent(),
                comment.getCreatedAt(),
                null, // 게시글 상세에는 postId 불필요
                null, // 게시글 상세에는 postTitle 불필요
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getChildren().stream()
                        .map(CommentResponseDto::forPost)
                        .collect(Collectors.toList())
        );
    }
}
