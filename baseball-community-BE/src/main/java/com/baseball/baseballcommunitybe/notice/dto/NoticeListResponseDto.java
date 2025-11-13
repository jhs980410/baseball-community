package com.baseball.baseballcommunitybe.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeListResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private boolean pinned;

    private int commentCount;
    private int likeCount;
    private int viewCount;
}
