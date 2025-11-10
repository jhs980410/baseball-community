package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminReport;
import com.baseball.baseballcommunitybe.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminReportRepository extends JpaRepository<AdminReport, Long> {
    long count();

    List<AdminReport> findTop100ByOrderByCreatedAtDesc();

    // 특정유저 신고 횟수 조회
    @Query("SELECT COUNT(r) FROM Report r WHERE r.targetId = :userId")
    int countReportsByUserId(@Param("userId") Long userId);


}