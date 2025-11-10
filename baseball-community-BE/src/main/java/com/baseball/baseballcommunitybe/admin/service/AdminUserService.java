package com.baseball.baseballcommunitybe.admin.service;


import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.repository.AdminCommentRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminPostRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminReportRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminReportRepository adminReportRepository;
    private final AdminCommentRepository adminCommentRepository;
    private final AdminPostRepository adminPostRepository;
    private final RedisTemplate<String, String> redisTemplate;
    public Page<AdminUserDto> findAllAsDto(Pageable pageable) {
        return adminUserRepository.findAllAsDto(pageable);
    }


    /**
     * 유저 상세조회 (신고수, 게시글수, 댓글수 포함)
     */
    public AdminUserDetailDto getUserDetail(Long userId) {
        // 1. 기본 유저 정보
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 부가 정보(신고, 게시글, 댓글 수)
        int reportCount = adminReportRepository.countReportsByUserId(userId);
        int postCount = adminPostRepository.countPostsByUserId(userId);
        int commentCount = adminCommentRepository.countCommentsByUserId(userId);

        // 3. DTO로 묶어서 반환
        return new AdminUserDetailDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole().name(),
                user.getStatus().name(),
                reportCount,
                postCount,
                commentCount,
                user.getCreatedAt()
        );
    }
    @Transactional
    public void updateUserStatus(Long userId, String newStatus) {
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setStatus(AdminUser.Status.valueOf(newStatus.toUpperCase()));
        adminUserRepository.save(user);
    }
    /**
     * 회원 정지
     */
    @Transactional
    public void suspendUser(Long userId, String reason, long durationHours) {
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setStatus(AdminUser.Status.SUSPENDED);
        user.setSuspendReason(reason);
        user.setSuspendedAt(LocalDateTime.now());

        // DB 업데이트
        adminUserRepository.save(user);

        // durationHours > 0이면 TTL 부여 (자동복구)
        if (durationHours > 0) {
            String key = "suspended:user:" + userId;
            redisTemplate.opsForValue().set(key, "SUSPENDED", durationHours, TimeUnit.HOURS);
        }
    }

    /**
     * 회원 복구
     */
    @Transactional
    public void unsuspendUser(Long userId) {
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        user.setStatus(AdminUser.Status.ACTIVE);
        user.setSuspendReason(null);
        user.setSuspendedAt(null);

        // Redis에서 TTL 제거
        redisTemplate.delete("suspended:user:" + userId);

        adminUserRepository.save(user);
    }

    /**
     * Redis TTL이 만료된 경우 자동 복구 (스케줄러 or 리스너에서 호출)
     */
    @Transactional
    public void restoreUser(Long userId) {
        adminUserRepository.findById(userId).ifPresent(user -> {
            user.setStatus(AdminUser.Status.ACTIVE);
            user.setSuspendReason(null);
            user.setSuspendedAt(null);
            adminUserRepository.save(user);
        });
    }


}


