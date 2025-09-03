package com.baseball.baseballcommunitybe.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String postTitle;
    private String userNickname;
    private LocalDateTime createdAt;
}