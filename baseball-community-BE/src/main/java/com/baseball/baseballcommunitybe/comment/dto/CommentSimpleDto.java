package com.baseball.baseballcommunitybe.comment.dto;

import com.baseball.baseballcommunitybe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentSimpleDto {
    private Long id;
    private String content;
    private String userNickname;
    private String createdAt;

    public static CommentSimpleDto from(Comment comment) {
        return new CommentSimpleDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreatedAt().toString()
        );
    }
}
