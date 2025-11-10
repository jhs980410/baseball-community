package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserCreateRequest;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 🔹 전체 관리자 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<AdminUserDto> getAllAdmins(Pageable pageable) {
        return adminUserRepository.findAllAsDto(pageable);
    }

    /**
     * 🔹 역할 기반 조회
     */
    @Transactional(readOnly = true)
    public List<AdminUserDto> getAdminsByRoles(List<String> roles) {
        return adminUserRepository.findByRoleIn(roles)
                .stream()
                .map(AdminUserDto::fromEntity)
                .toList();
    }

    /**
     * 🔹 새 관리자 생성 (기본 role: ADMIN)
     */
    public AdminUserDto createAdmin(AdminUserCreateRequest req) {
        if (adminUserRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다: " + req.getEmail());
        }

        AdminUser admin = AdminUser.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(AdminUser.Role.ADMIN)
                .status(AdminUser.Status.ACTIVE)
                .build();

        adminUserRepository.save(admin);
        return AdminUserDto.fromEntity(admin);
    }

    /**
     * 🔹 권한 변경
     */
    public AdminUserDto updateAdminRole(Long id, AdminUser.Role newRole) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다. ID: " + id));

        if (admin.getRole() == AdminUser.Role.SUPER_ADMIN) {
            throw new IllegalStateException("SUPER_ADMIN의 권한은 변경할 수 없습니다.");
        }

        admin.setRole(newRole);
        adminUserRepository.save(admin);

        return AdminUserDto.fromEntity(admin);
    }

    /**
     * 🔹 관리자 삭제
     */
    public void deleteAdmin(Long id) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다. ID: " + id));

        if (admin.getRole() == AdminUser.Role.SUPER_ADMIN) {
            throw new IllegalStateException("SUPER_ADMIN 계정은 삭제할 수 없습니다.");
        }

        adminUserRepository.delete(admin);
    }

    /**
     *  SUPER_ADMIN 권한 위임
     * 기존 SUPER_ADMIN → ADMIN 강등
     * 대상 ADMIN → SUPER_ADMIN 승급
     */
    public void transferSuperAdmin(Long fromId, Long toId) {
        AdminUser from = adminUserRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("기존 SUPER_ADMIN을 찾을 수 없습니다."));
        AdminUser to = adminUserRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("새 관리자 계정을 찾을 수 없습니다."));

        // 1️⃣ 권한 검증
        if (from.getRole() != AdminUser.Role.SUPER_ADMIN) {
            throw new IllegalStateException("SUPER_ADMIN 권한이 있는 계정만 위임할 수 있습니다.");
        }
        if (to.getRole() == AdminUser.Role.SUPER_ADMIN) {
            throw new IllegalStateException("이미 SUPER_ADMIN 계정입니다.");
        }

        // 2️⃣ 현재 SUPER_ADMIN 수 검증 (항상 1명만 존재)
        long count = adminUserRepository.countByRole(AdminUser.Role.SUPER_ADMIN);
        if (count != 1) {
            throw new IllegalStateException("SUPER_ADMIN 계정 수가 비정상적입니다. (현재 " + count + "명)");
        }

        // 3️⃣ 권한 동시 변경 (트랜잭션 보장)
        from.setRole(AdminUser.Role.ADMIN);
        to.setRole(AdminUser.Role.SUPER_ADMIN);

        adminUserRepository.save(from);
        adminUserRepository.save(to);
    }
}
