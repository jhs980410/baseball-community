package com.baseball.baseballcommunitybe.admin.dto.user;

import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminUserDto {

    private Long id;
    private String email;
    private String nickname;
    private AdminUser.Role role;
    private AdminUser.Status status;
    private LocalDateTime createdAt;

    /**
     * Entity → DTO 변환 메서드
     */
    public static AdminUserDto fromEntity(AdminUser entity) {
        return new AdminUserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public AdminUserDto(AdminUser adminUser) {
        this.id = adminUser.getId();
        this.email = adminUser.getEmail();
        this.nickname = adminUser.getNickname();
        this.role = adminUser.getRole();
        this.status = adminUser.getStatus();
        this.createdAt = adminUser.getCreatedAt();
    }

}
