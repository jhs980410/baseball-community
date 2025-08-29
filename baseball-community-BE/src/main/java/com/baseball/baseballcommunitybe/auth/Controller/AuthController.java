package com.baseball.baseballcommunitybe.auth.Controller;

import com.baseball.baseballcommunitybe.auth.Dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.Dto.SignupRequestDto;
import com.baseball.baseballcommunitybe.auth.Dto.TokenResponse;

import com.baseball.baseballcommunitybe.auth.Service.AuthService;
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    // ---------------- 로그인 ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(dto);

        // JWT를 HttpOnly 쿠키로 저장
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", tokenResponse.getToken())
                .httpOnly(true)       // JS 접근 차단
                .secure(false)        // 배포(HTTPS) 시 true
                .sameSite("Lax")      // CSRF 방어 기본값
                .path("/")            // 모든 경로에서 쿠키 사용 가능
                .maxAge(60 * 60)      // 1시간 유효
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(tokenResponse);
    }
}
