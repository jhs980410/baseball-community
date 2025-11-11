package com.baseball.baseballcommunitybe.admin.dto.post;

import com.baseball.baseballcommunitybe.admin.entity.AdminPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPostDetailDto {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long teamId;

    // 상태 관련 필드
    private Long commentCount;
    private Long likeCount;
    private Long viewCount;
    private Long reportCount;
    private Boolean containsBannedWord;
    private Boolean flagged;
    private String lastFlagReason;

    //  추가: posts.is_hidden 컬럼 값 (AdminPost 엔티티와 직접 매핑 안 함)
    private Boolean isHidden;

    /** 엔티티 → DTO 변환 (Null-safe) */
    public static AdminPostDetailDto fromEntity(AdminPost entity) {

        if (entity.getStatus() == null) {
            return AdminPostDetailDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .userId(entity.getUserId())
                    .teamId(entity.getTeamId())
                    .commentCount(0L)
                    .likeCount(0L)
                    .viewCount(0L)
                    .reportCount(0L)
                    .containsBannedWord(false)
                    .flagged(false)
                    .lastFlagReason(null)
                    // isHidden은 추후 Query에서 채워짐
                    .build();
        }

        return AdminPostDetailDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .userId(entity.getUserId())
                .teamId(entity.getTeamId())
                .commentCount(entity.getStatus().getCommentCount())
                .likeCount(entity.getStatus().getLikeCount())
                .viewCount(entity.getStatus().getViewCount())
                .reportCount(entity.getStatus().getReportCount())
                .containsBannedWord(entity.getStatus().isContainsBannedWord())
                .flagged(entity.getStatus().isFlagged())
                .lastFlagReason(entity.getStatus().getLastFlagReason())
                // isHidden은 Query 결과로 채움
                .build();
    }
    public AdminPostDetailDto(
            Long id, String title, String content,
            Long userId, Long teamId,
            Number commentCount, Number likeCount, Number viewCount, Number reportCount,
            Boolean containsBannedWord, Boolean flagged,
            String lastFlagReason, Boolean isHidden
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.teamId = teamId;
        this.commentCount = commentCount != null ? commentCount.longValue() : 0L;
        this.likeCount = likeCount != null ? likeCount.longValue() : 0L;
        this.viewCount = viewCount != null ? viewCount.longValue() : 0L;
        this.reportCount = reportCount != null ? reportCount.longValue() : 0L;
        this.containsBannedWord = containsBannedWord != null && containsBannedWord;
        this.flagged = flagged != null && flagged;
        this.lastFlagReason = lastFlagReason;
        this.isHidden = isHidden != null && isHidden;
    }

}
