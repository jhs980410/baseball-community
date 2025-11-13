package com.baseball.baseballcommunitybe.admin.dto.notice;

import com.baseball.baseballcommunitybe.admin.entity.AdminNotice;
import lombok.Getter;

@Getter
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private boolean isPinned;
    private String createdAt;
    private String updatedAt;

    public NoticeDto(AdminNotice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.isPinned = notice.isPinned();
        this.createdAt = notice.getCreatedAt().toString();
        this.updatedAt = notice.getUpdatedAt().toString();
    }
}
