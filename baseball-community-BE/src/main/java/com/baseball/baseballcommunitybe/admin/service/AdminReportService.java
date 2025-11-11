package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminReport;
import com.baseball.baseballcommunitybe.admin.repository.AdminReportRepository;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final AdminReportRepository adminReportRepository;

    public List<AdminReportDto> getReportsByTargetType(ReportTargetType targetType) {
        List<AdminReport> reports = adminReportRepository.findByTargetTypeOrderByCreatedAtDesc(targetType);
        return reports.stream()
                .map(AdminReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteReport(Long id) {
        adminReportRepository.deleteById(id);
    }

    // 이후 신고 처리 상태 업데이트 (예: reviewed → resolved)
    public void updateReportStatus(Long id, String newStatus) {
        AdminReport report = adminReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));
        report.setStatus(Enum.valueOf(com.baseball.baseballcommunitybe.report.entity.ReportStatus.class, newStatus.toUpperCase()));
        adminReportRepository.save(report);
    }
}
