package com.baseball.baseballcommunitybe.auth.dto;

import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.user.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private String email;
    private String nickname;
    private String role; // ✅ 통일된 String 타입으로 저장

    // 오버로딩 생성자 ① (일반 User)
    public TokenResponseDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
    }

    // 오버로딩 생성자 ② (AdminUser)
    public TokenResponseDto(String accessToken, String refreshToken, AdminUser admin) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = admin.getId();
        this.email = admin.getEmail();
        this.nickname = admin.getNickname();
        this.role = admin.getRole().name();
    }

    // 빌더용 보조 생성자 (명시적으로 모든 필드 수동 세팅 시)
    @Builder
    public TokenResponseDto(
            String accessToken,
            String refreshToken,
            Long id,
            String email,
            String nickname,
            String role
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
