package com.baseball.baseballcommunitybe.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
}