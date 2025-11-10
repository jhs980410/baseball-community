package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.TokenResponseDto;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 관리자 로그인
     * POST /api/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        TokenResponseDto tokens = authService.login(dto); // 기존 로그인 로직 재사용

        // JWT에서 role 추출
        String role = jwtTokenProvider.getRoleFromToken(tokens.getAccessToken());

        // 관리자 계정만 로그인 허용
        if (!role.equalsIgnoreCase("SUPER_ADMIN") && !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(403).body("관리자 권한이 없습니다.");
        }


        // ADMIN_TOKEN 쿠키 생성 (HttpOnly)
        ResponseCookie cookie = ResponseCookie.from("ADMIN_TOKEN", tokens.getAccessToken())
                .httpOnly(true)
                .secure(false) // 운영환경에서는 true (HTTPS)
                .sameSite("Lax")
                .path("/")
                .maxAge(60 * 15) // 15분
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(tokens);
    }

    /**
     * 관리자 로그아웃
     * DELETE /api/admin/auth/logout
     */
    @DeleteMapping("/logout")
    public ResponseEntity<Void> adminLogout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("관리자 로그아웃 실행됨");

        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("관리자 토큰 = " + token);
        authService.logout(token);

        ResponseCookie expiredCookie = ResponseCookie.from("ADMIN_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax") // 로컬에서는 None
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", expiredCookie.toString());
        return ResponseEntity.noContent().build();
    }

}

