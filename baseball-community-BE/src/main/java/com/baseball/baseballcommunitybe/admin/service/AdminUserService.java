package com.baseball.baseballcommunitybe.admin.service;


import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.repository.AdminCommentRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminPostRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminReportRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminReportRepository adminReportRepository;
    private final AdminCommentRepository adminCommentRepository;
    private final AdminPostRepository adminPostRepository;
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

}


