package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportHandleRequestDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminReport;
import com.baseball.baseballcommunitybe.admin.repository.*;
import com.baseball.baseballcommunitybe.report.entity.ReportStatus;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final AdminReportRepository adminReportRepository;

    // ✅ 관리자 전용 Repository 계층 의존성
    private final AdminPostRepository adminPostRepository;
    private final AdminCommentRepository adminCommentRepository;
    private final AdminUserRepository adminUserRepository;
    private final AdminLogRepository adminLogRepository;

    // ---------------------------------------------------
    // 기존 코드 유지 (조회 / 삭제 / 상태 변경)
    // ---------------------------------------------------

    public List<AdminReportDto> getReportsByTargetType(ReportTargetType targetType) {
        List<AdminReport> reports = adminReportRepository.findByTargetTypeOrderByCreatedAtDesc(targetType);
        return reports.stream()
                .map(AdminReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteReport(Long id) {
        adminReportRepository.deleteById(id);
    }

    public void updateReportStatus(Long id, String newStatus) {
        AdminReport report = adminReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));
        report.setStatus(Enum.valueOf(ReportStatus.class, newStatus.toUpperCase()));
        adminReportRepository.save(report);
    }

    // ---------------------------------------------------
    //  신규 추가: 관리자 신고 조치 처리
    // ---------------------------------------------------

    @Transactional
    public void handleReport(Long reportId, AdminReportHandleRequestDto request, Long adminId) {
        AdminReport report = adminReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));

        String action = request.getAction();
        ReportTargetType type = report.getTargetType();
        Long targetId = report.getTargetId();

        switch (type) {
            case POST -> handlePostAction(targetId, action, request.getAdminNote(), adminId);
            case COMMENT -> handleCommentAction(targetId, action, request.getAdminNote(), adminId);
            case USER -> handleUserAction(targetId, action, request.getAdminNote(), adminId);
        }

        report.setStatus(ReportStatus.RESOLVED);
        adminReportRepository.save(report);
    }


    // ---------------------------------------------------
    // 게시글 조치
    // ---------------------------------------------------
    private void handlePostAction(Long postId, String action, String reason,Long adminId) {
        switch (action) {
            case "HIDE_POST" -> adminPostRepository.hidePost(postId); // 숨김
            case "DELETE_POST" -> adminPostRepository.adminSoftDelete(postId); // soft delete
            case "WARN_USER" -> {
                Long userId = adminPostRepository.findAuthorIdByPostId(postId);
                adminUserRepository.warnUser(userId, "POST", reason);
            }
        }
        adminLogRepository.insertLog(adminId,"post", postId, action);
    }

    // ---------------------------------------------------
    // 댓글 조치
    // ---------------------------------------------------
    private void handleCommentAction(Long commentId, String action, String reason,Long adminId) {
        switch (action) {
            case "HIDE_COMMENT" -> adminCommentRepository.hideComment(commentId);
            case "DELETE_COMMENT" -> adminCommentRepository.softDelete(commentId);
            case "WARN_USER" -> {
                Long userId = adminCommentRepository.findAuthorIdByCommentId(commentId);
                adminUserRepository.warnUser(userId, "COMMENT", reason);
            }
        }
        adminLogRepository.insertLog(adminId,"comment", commentId, action);
    }

    // ---------------------------------------------------
    // 사용자 조치
    // ---------------------------------------------------
    private void handleUserAction(Long userId, String action, String reason,Long adminId) {
        switch (action) {
            case "WARN_USER" -> adminUserRepository.warnUser(userId, "ALL", reason);
            case "SUSPEND_USER" -> adminUserRepository.suspendUser(userId, 7, reason);
            case "BAN_USER" -> adminUserRepository.banUser(userId);
        }
        adminLogRepository.insertLog(adminId,"user", userId, action);
    }
}
