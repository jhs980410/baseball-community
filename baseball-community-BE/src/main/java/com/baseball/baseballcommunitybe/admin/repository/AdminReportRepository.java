package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminReport;
import com.baseball.baseballcommunitybe.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminReportRepository extends JpaRepository<AdminReport, Long> {
    long count();

    List<AdminReport> findTop100ByOrderByCreatedAtDesc();
}