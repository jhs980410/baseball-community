package com.baseball.baseballcommunitybe.comment.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime date;
    private Long postId;
    private String postTitle;

    // 마이페이지 전용 팩토리
    public static CommentResponseDto forUser(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,  // userId
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getPost().getTitle()
        );
    }

    // 게시글 상세 전용 팩토리
    public static CommentResponseDto forPost(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,  // userId
                comment.getContent(),
                comment.getCreatedAt(),
                null,   // 게시글 상세에는 postId 불필요
                null    // 게시글 상세에는 postTitle 불필요
        );
    }

}