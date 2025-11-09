package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 관리자용 전체 유저 목록 조회 (페이징)
     * 예: GET /api/admin/users?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<AdminUser>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminUser> users = adminUserService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
}
