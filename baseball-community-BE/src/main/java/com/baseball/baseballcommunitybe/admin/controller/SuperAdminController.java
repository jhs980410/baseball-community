package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserCreateRequest;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserRoleUpdateRequest;
import com.baseball.baseballcommunitybe.admin.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super/admins")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    /**
     * 🔹 전체 관리자 목록 조회 (페이지네이션 포함)
     */
    @GetMapping
    public ResponseEntity<Page<AdminUserDto>> getAllAdmins(Pageable pageable) {
        Page<AdminUserDto> admins = superAdminService.getAllAdmins(pageable);
        return ResponseEntity.ok(admins);
    }

    /**
     *  역할 기반 조회 (ADMIN, SUPER_ADMIN)
     */
    @GetMapping("/roles")
    public ResponseEntity<List<AdminUserDto>> getAdminsByRoles(
            @RequestParam List<String> roles
    ) {
        List<AdminUserDto> admins = superAdminService.getAdminsByRoles(roles);
        return ResponseEntity.ok(admins);
    }

    /**
     *  새 관리자 생성
     */
    @PostMapping
    public ResponseEntity<AdminUserDto> createAdmin(@RequestBody AdminUserCreateRequest req) {
        AdminUserDto created = superAdminService.createAdmin(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     *  관리자 권한 변경 (ADMIN ⇄ SUPER_ADMIN)
     */
    @PatchMapping("/{id}/role")
    public ResponseEntity<AdminUserDto> updateAdminRole(
            @PathVariable Long id,
            @RequestBody AdminUserRoleUpdateRequest req
    ) {
        AdminUserDto updated = superAdminService.updateAdminRole(id, req.getRole());
        return ResponseEntity.ok(updated);
    }

    /**
     *  관리자 계정 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        superAdminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 🔹 SUPER_ADMIN 권한 위임 API
     * 기존 SUPER_ADMIN(fromId)을 ADMIN(toId)에게 위임
     *
     * ex) POST /api/super/admins/transfer?fromId=1&toId=5
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transferSuperAdmin(
            @RequestParam Long fromId,
            @RequestParam Long toId
    ) {
        superAdminService.transferSuperAdmin(fromId, toId);
        return ResponseEntity.ok("✅ SUPER_ADMIN 권한이 성공적으로 위임되었습니다.");
    }

}
