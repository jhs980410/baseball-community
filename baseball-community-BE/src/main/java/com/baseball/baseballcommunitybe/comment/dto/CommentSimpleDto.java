package com.baseball.baseballcommunitybe.comment.dto;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentSimpleDto {
    private Long id;
    private String content;
    private String userNickname;
    private String createdAt;
    private Long parentId;
    private List<CommentSimpleDto> children; // 🔹 타입 수정

    public static CommentSimpleDto from(Comment comment) {
        return new CommentSimpleDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreatedAt().toString(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getChildren().stream()
                        .map(CommentSimpleDto::from)
                        .collect(Collectors.toList())
        );
    }
}
