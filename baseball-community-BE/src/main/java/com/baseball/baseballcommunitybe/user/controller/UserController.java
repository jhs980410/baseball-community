package com.baseball.baseballcommunitybe.user.controller;

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


    /**
     * 내 정보 조회 (쿠키에 담긴 JWT 기반)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(HttpServletRequest request) {
        UserResponseDto response = userService.getMyProfile(request);
        return ResponseEntity.ok(response);
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
        userService.updateMyProfile(request, dto);
        return ResponseEntity.ok("내 정보 수정 완료");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(HttpServletRequest request) {
        System.out.println("호출됨");
        userService.deleteMyAccount(request);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    /**
     * 관리자: 특정 유저 상태 변경 (active/suspended/deleted)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok("상태 변경 완료");
    }

}
