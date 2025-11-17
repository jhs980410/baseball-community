package com.baseball.baseballcommunitybe.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN, MODERATOR, SUPER_ADMIN;

        public static Role from(String value) {
            return Role.valueOf(value.toUpperCase());
        }
    }

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, SUSPENDED, DELETED
    }

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    public void setRefreshToken(String newRefresh) {
        this.refreshToken = newRefresh;
    }

    // 🔥 Soft Delete 컬럼 추가
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 🔥 소프트 삭제 처리 메서드
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED;
    }
}
