package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import com.baseball.baseballcommunitybe.admin.service.AdminReportService;
import com.baseball.baseballcommunitybe.report.entity.ReportStatus;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    // 📄 게시글 신고 조회
    @GetMapping("/posts")
    public List<AdminReportDto> getPostReports() {
        return adminReportService.getReportsByTargetType(ReportTargetType.POST);
    }

    // 💬 댓글 신고 조회
    @GetMapping("/comments")
    public List<AdminReportDto> getCommentReports() {
        return adminReportService.getReportsByTargetType(ReportTargetType.COMMENT);
    }

    // 🙍 사용자 신고 조회
    @GetMapping("/users")
    public List<AdminReportDto> getUserReports() {
        return adminReportService.getReportsByTargetType(ReportTargetType.USER);
    }

    // 신고 삭제
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        adminReportService.deleteReport(id);
    }

    // 신고 상태 변경 (예: pending → reviewed)
    @PatchMapping("/{id}/status")
    public void updateReportStatus(
            @PathVariable Long id,
            @RequestParam("status") String status
    ) {
        adminReportService.updateReportStatus(id, status);
    }
}
