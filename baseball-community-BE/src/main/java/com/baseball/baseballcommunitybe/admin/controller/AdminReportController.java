package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportHandleRequestDto;
import com.baseball.baseballcommunitybe.admin.service.AdminReportService;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.report.entity.ReportStatus;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;
    private final JwtTokenProvider jwtTokenProvider;
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

    // ⚖️ (신규 추가) 신고 처리 — 관리자 조치 수행
    @PatchMapping("/{reportId}/handle")
    public ResponseEntity<?> handleReport(
            @PathVariable Long reportId,
            @RequestBody AdminReportHandleRequestDto request,
            HttpServletRequest httpRequest) {

        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token == null) {
            return ResponseEntity.status(401).body("유효하지 않은 관리자 토큰입니다.");
        }
        Long adminId = jwtTokenProvider.getUserIdFromToken(token);  //  직접 추출

        adminReportService.handleReport(reportId, request, adminId);
        return ResponseEntity.ok("조치 완료");
    }


}
