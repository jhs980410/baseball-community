package com.baseball.baseballcommunitybe.user.controller;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.user.dto.UserResponseDto;
import com.baseball.baseballcommunitybe.user.dto.UserUpdateRequestDto;
import com.baseball.baseballcommunitybe.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 내 정보 조회 (JWT 쿠키 기반)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        return ResponseEntity.ok(userService.getUser(userId));
    }


    /**
     * 특정 사용자 프로필 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    /**
     * 내 정보 수정 (닉네임, 비밀번호 변경 등)
     */
    @PutMapping("/me")
    public ResponseEntity<String> updateMyProfile(
            HttpServletRequest request,
            @RequestBody UserUpdateRequestDto dto
    ) {
        Long userId = extractUserId(request);
        userService.updateUser(userId, dto);
        return ResponseEntity.ok("내 정보 수정 완료");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(HttpServletRequest request) {
        Long userId = extractUserId(request);
        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    /**
     * 관리자: 특정 유저 상태 변경 (ACTIVE/SUSPENDED/DELETED)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long id,
            @RequestBody String status
    ) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok("상태 변경 완료");
    }

    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(!userService.existsByEmail(email));
    }

    /**
     * 닉네임 중복 확인
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(!userService.existsByNickname(nickname));
    }

    // ------------------ 내부 헬퍼 ------------------
    private Long extractUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("인증 토큰이 없습니다.");
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
