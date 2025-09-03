package com.baseball.baseballcommunitybe.like.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LikeResponseDto {
    private Long postId;
    private String title;
    private String author;
    private LocalDateTime date;
}
