package com.baseball.baseballcommunitybe.notice.dto;

import com.baseball.baseballcommunitybe.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private boolean isPinned;
    private String createdAt;
    private String updatedAt;
    private int commentCount;
    private int likeCount;
    private int viewCount;

    // 엔티티 → 상세페이지 변환
    public static NoticeResponseDto fromEntity(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .isPinned(notice.isPinned())
                .createdAt(notice.getCreatedAt().toString())
                .updatedAt(notice.getUpdatedAt().toString())
                .commentCount(0)
                .likeCount(0)
                .viewCount(0)
                .build();
    }

    // 리스트용 DTO → 공용 NoticeResponseDto 변환
    public static NoticeResponseDto fromListDto(NoticeListResponseDto dto) {
        return NoticeResponseDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(null)
                .isPinned(dto.isPinned())
                .createdAt(dto.getCreatedAt().toString())
                .updatedAt(null)
                .commentCount(dto.getCommentCount())
                .likeCount(dto.getLikeCount())
                .viewCount(dto.getViewCount())
                .build();
    }
}
