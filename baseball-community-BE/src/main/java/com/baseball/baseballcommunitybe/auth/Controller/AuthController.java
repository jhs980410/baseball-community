package com.baseball.baseballcommunitybe.auth.Controller;

import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
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
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

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

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response
    ) {
        TokenResponseDto tokens = authService.login(dto);

        // Access Token → HttpOnly 쿠키에 저장
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", tokens.getAccessToken())
                .httpOnly(true)
                .secure(false) // 배포 시 true (HTTPS)
                .sameSite("Lax")
                .path("/")
                .maxAge(60 * 15) // 15분
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(tokens);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        authService.logout(accessToken);
        return ResponseEntity.ok().build();
    }
    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestParam Long userId) {
        TokenResponseDto tokens = authService.refresh(userId);
        return ResponseEntity.ok(tokens);
    }



}
