package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.dashboard.DashboardResponse;
import com.baseball.baseballcommunitybe.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboards")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * 관리자 대시보드 조회
     * GET /api/admin/dashboards
     */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse response = adminDashboardService.getDashboardData();
        return ResponseEntity.ok(response);
    }
}
