package com.baseball.baseballcommunitybe.admin.dto.notice;

import lombok.Getter;

@Getter
public class NoticeCreateRequestDto {
    private String title;
    private String content;
    private boolean isPinned;
}