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
    private Long id;
    private Long reporterId;
    private String reason;
    private String targetType;
    private Long targetId;
    private LocalDateTime createdAt;

    public static AdminReportDto fromEntity(AdminReport entity) {
        return AdminReportDto.builder()
                .id(entity.getId())
                .reporterId(entity.getReporter().getId())   //
                .reason(entity.getReason())
                .targetType(entity.getTargetType())
                .targetId(entity.getTargetId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
