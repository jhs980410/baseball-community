package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminDailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdminDailyStatsRepository extends JpaRepository<AdminDailyStats, LocalDate> {
    List<AdminDailyStats> findTop7ByOrderByStatDateDesc();
}
