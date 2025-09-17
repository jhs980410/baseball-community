package com.baseball.baseballcommunitybe.report.repository;

import com.baseball.baseballcommunitybe.report.entity.Report;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import com.baseball.baseballcommunitybe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByTargetTypeAndTargetIdAndReporter(
            ReportTargetType targetType,
            Long targetId,
            User reporter
    );
}
