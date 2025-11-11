package com.baseball.baseballcommunitybe.admin.dto.report;

import com.baseball.baseballcommunitybe.admin.entity.AdminReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReportDto {
    private Long id;                // 신고 ID
    private Long reporterId;        // 신고자 ID
    private String reason;          // 신고 사유 (enum → string)
    private String targetType;      // 신고 대상 타입 (enum → string)
    private Long targetId;          // 대상 ID
    private String status;          // 신고 처리 상태 (추가)
    private LocalDateTime createdAt;// 생성일시

    public static AdminReportDto fromEntity(AdminReport entity) {
        return AdminReportDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporter().getId())
                .reason(entity.getReason().name())          // Enum → String 변환
                .targetType(entity.getTargetType().name())  // Enum → String 변환
                .targetId(entity.getTargetId())
                .status(entity.getStatus().name())          // Enum → String 변환
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
