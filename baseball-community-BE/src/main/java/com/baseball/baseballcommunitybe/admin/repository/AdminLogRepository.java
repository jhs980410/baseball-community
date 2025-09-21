package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
    List<AdminLog> findTop20ByOrderByCreatedAtDesc();
}
