package com.baseball.baseballcommunitybe.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private String content;
    private Long parentId;  // 대댓글일 경우 부모 댓글 id (없으면 null)
}