package com.baseball.baseballcommunitybe.auth.Controller;

import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.SignupRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.TokenResponse;

import com.baseball.baseballcommunitybe.auth.Service.AuthService;
import com.baseball.baseballcommunitybe.user.service.UserService;
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
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }
    // 이메일 중복확인
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean available = !userService.existsByEmail(email);
        return ResponseEntity.ok(available);
    }

    // 닉네임 중복확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean available = !userService.existsByNickname(nickname);
        return ResponseEntity.ok(available);
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
