package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
    List<AdminLog> findTop20ByOrderByCreatedAtDesc();
    @Modifying
    @Query(value = """
        INSERT INTO admin_logs (admin_id, target_type, target_id, action, created_at)
        VALUES (:adminId, :targetType, :targetId, :action, NOW())
    """, nativeQuery = true)
    void insertLog(
            @Param("adminId") Long adminId,
            @Param("targetType") String targetType,
            @Param("targetId") Long targetId,
            @Param("action") String action
    );
}
