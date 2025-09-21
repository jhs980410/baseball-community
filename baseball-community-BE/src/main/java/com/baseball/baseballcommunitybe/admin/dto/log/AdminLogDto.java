package com.baseball.baseballcommunitybe.admin.dto.log;

import com.baseball.baseballcommunitybe.admin.entity.AdminLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLogDto {
    private Long id;
    private String action;
    private String targetType;
    private Long targetId;
    private Long adminId;
    private LocalDateTime createdAt;

    public static AdminLogDto fromEntity(AdminLog entity) {
        return AdminLogDto.builder()
                .id(entity.getId())
                .action(entity.getAction())
                .targetType(entity.getTargetType())
                .targetId(entity.getTargetId())
                .adminId(entity.getAdminId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}