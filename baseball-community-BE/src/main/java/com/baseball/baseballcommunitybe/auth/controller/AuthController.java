package com.baseball.baseballcommunitybe.auth.controller;

import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.PasswordRequest;
import com.baseball.baseballcommunitybe.auth.dto.SignupRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.TokenResponseDto;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.auth.service.AuthService;
import com.baseball.baseballcommunitybe.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        System.out.println("회원가입시작");
        authService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        TokenResponseDto tokens = authService.login(dto);

        // Access Token → HttpOnly 쿠키 저장
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", tokens.getAccessToken())
                .httpOnly(true)
                .secure(true) // 운영 시 true (HTTPS)
                .sameSite("None")
                .path("/")
                .domain("baseballjhs.o-r.kr")
                .maxAge(60 * 15) // 15분
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(tokens);
    }

    /**
     * 로그아웃 (Access Token 무효화)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response, HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        authService.logout(accessToken); // 서버 측 블랙리스트/로그 기록 처리

        // 쿠키 만료 처리
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(true) // 운영 시 true (HTTPS)
                .sameSite("None")
                .domain("baseballjhs.o-r.kr")
                .path("/")
                .maxAge(0) // 즉시 만료
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().build();
    }
    /**
     * 토큰 재발급 (Refresh Token 기반)
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(
            @RequestHeader("Authorization") String bearerToken
    ) {
        String refreshToken = bearerToken.replace("Bearer ", "");
        TokenResponseDto tokens = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokens);
    }
    /**
     * 비밀번호 확인
     */
    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(
            @RequestBody PasswordRequest req,
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request); // 쿠키에서 꺼내기
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        System.out.println("유저id:" + userId);
        System.out.println("입력비번:" + req.getPassword());
        System.out.println("token:" + token);
        boolean result = authService.verifyPassword(userId, req.getPassword());

        return ResponseEntity.ok(result);
    }

}
