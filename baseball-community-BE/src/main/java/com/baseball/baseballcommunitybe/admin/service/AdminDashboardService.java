package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.dailyStats.DailyStatsDto;
import com.baseball.baseballcommunitybe.admin.dto.dashboard.DashboardResponse;
import com.baseball.baseballcommunitybe.admin.dto.log.AdminLogDto;
import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto;
import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import com.baseball.baseballcommunitybe.admin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AdminDailyStatsRepository adminDailyStatsRepository;
    private final AdminPostRepository adminPostRepository;
    private final AdminReportRepository adminReportRepository;
    private final AdminLogRepository adminLogRepository;
    private final AdminUserRepository adminUserRepository; //  user 수용

    public DashboardResponse getDashboardData() {
        long totalUsers = adminUserRepository.count();  // 전체 회원 수
        long totalPosts = adminPostRepository.count();  // 전체 게시글 수
        long totalReports = adminReportRepository.count(); // 전체 신고 수

        return DashboardResponse.builder()
                // 요약 통계
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalReports(totalReports)

                // 최근 7일 통계
                .dailyStats(adminDailyStatsRepository.findTop7ByOrderByStatDateDesc()
                        .stream().map(DailyStatsDto::fromEntity).toList())

                // 인기글 Top 5
                .topPosts(adminPostRepository.findTop5ByOrderByStatus_LikeCountDesc()
                        .stream().map(AdminPostDto::fromEntity).toList())

                // 최근 신고 Top 100
                .reports(adminReportRepository.findTop100ByOrderByCreatedAtDesc()
                        .stream().map(AdminReportDto::fromEntity).toList())

                // 관리자 로그 최근 20개
                .adminLogs(adminLogRepository.findTop20ByOrderByCreatedAtDesc()
                        .stream().map(AdminLogDto::fromEntity).toList())
                .build();
    }
}