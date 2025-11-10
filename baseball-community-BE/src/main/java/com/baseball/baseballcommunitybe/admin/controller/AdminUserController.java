package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.service.AdminUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 전체 회원 목록 (페이징)
     * GET /api/admin/users?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<AdminUserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nickname
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminUserDto> users = adminUserService.findAllAsDto(nickname, pageable);
        return ResponseEntity.ok(users);
    }



    /**
     * 회원 상세 조회 (신고 수, 글 수, 댓글 수 포함)
     * GET /api/admin/users/{id}/detail
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDetailDto> getUserDetail(@PathVariable Long id) {
        AdminUserDetailDto detail = adminUserService.getUserDetail(id);
        return ResponseEntity.ok(detail);
    }

    /**
     * 회원 상태 변경 (정지, 복구 등)
     * PATCH /api/admin/users/{id}/status
     */
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request
    ) {
        adminUserService.updateUserStatus(id, request.getStatus());
        return ResponseEntity.noContent().build();
    }

    // 내부 static DTO (요청 바디용)
    @Getter
    @Setter
    public static class UpdateStatusRequest {
        private String status; // 예: "SUSPENDED" or "ACTIVE"
    }

    /** 기존 목록 / 상세 / 상태 변경은 그대로 유지 **/

    /**
     * 회원 정지
     * PATCH /api/admin/users/{id}/suspend
     */
    @PatchMapping("/{id}/suspend")
    @Transactional
    public ResponseEntity<Void> suspendUser(
            @PathVariable Long id,
            @RequestBody SuspendRequest request
    ) {
        adminUserService.suspendUser(id, request.getReason(), request.getDurationHours());
        return ResponseEntity.noContent().build();
    }

    /**
     * 회원 정지 해제
     * PATCH /api/admin/users/{id}/unsuspend
     */
    @PatchMapping("/{id}/unsuspend")
    @Transactional
    public ResponseEntity<Void> unsuspendUser(@PathVariable Long id) {
        adminUserService.unsuspendUser(id);
        return ResponseEntity.noContent().build();
    }

    /** 요청 DTOs **/
    @Getter
    @Setter
    public static class SuspendRequest {
        private String reason;
        private long durationHours; // 0이면 영구정지
    }
}
