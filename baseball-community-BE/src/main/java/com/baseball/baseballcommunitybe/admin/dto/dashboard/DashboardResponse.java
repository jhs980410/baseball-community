package com.baseball.baseballcommunitybe.admin.dto.dashboard;

import com.baseball.baseballcommunitybe.admin.dto.dailyStats.DailyStatsDto;
import com.baseball.baseballcommunitybe.admin.dto.log.AdminLogDto;
import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto;
import com.baseball.baseballcommunitybe.admin.dto.report.AdminReportDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private long totalUsers;    // 전체 회원 수
    private long totalPosts;    // 전체 게시글 수
    private long totalReports;  // 전체 신고 건수

    private List<DailyStatsDto> dailyStats;
    private List<AdminPostDto> topPosts;
    private List<AdminReportDto> reports;
    private List<AdminLogDto> adminLogs;
}