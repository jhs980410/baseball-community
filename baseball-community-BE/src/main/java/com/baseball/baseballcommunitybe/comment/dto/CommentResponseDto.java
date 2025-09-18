package com.baseball.baseballcommunitybe.comment.dto;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String userNickname;   // 닉네임 추가
    private String content;
    private LocalDateTime createdAt;
    private Long postId;
    private String postTitle;
    private Long parentId;
    private List<CommentResponseDto> children;

    private boolean edited;        // 수정 여부
    private LocalDateTime editedAt; // 마지막 수정 시각

    // 📌 마이페이지용
    public static CommentResponseDto forUser(Comment comment, boolean edited, LocalDateTime editedAt) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getNickname() : "탈퇴회원",
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getPost().getTitle(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                null,
                edited,
                editedAt
        );
    }

    // 📌 게시글 상세용 (트리)
    public static CommentResponseDto forPost(Comment comment, boolean edited, LocalDateTime editedAt, List<CommentResponseDto> children) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getNickname() : "탈퇴회원",
                comment.getContent(),
                comment.getCreatedAt(),
                null,
                null,
                comment.getParent() != null ? comment.getParent().getId() : null,
                children,
                edited,
                editedAt
        );
    }
}
