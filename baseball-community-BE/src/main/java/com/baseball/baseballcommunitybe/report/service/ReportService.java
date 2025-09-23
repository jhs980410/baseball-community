package com.baseball.baseballcommunitybe.report.service;

import com.baseball.baseballcommunitybe.redis.repository.DailyStatsRedisRepository;
import com.baseball.baseballcommunitybe.report.dto.ReportRequestDto;
import com.baseball.baseballcommunitybe.report.entity.*;
import com.baseball.baseballcommunitybe.report.repository.ReportRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final DailyStatsRedisRepository dailyStatsRedisRepository;
    @Transactional
    public void report(Long targetId, ReportTargetType type, ReportRequestDto dto, Long userId) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 중복 신고 방지
        reportRepository.findByTargetTypeAndTargetIdAndReporter(type, targetId, reporter)
                .ifPresent(r -> {
                    throw new IllegalStateException("이미 신고한 대상입니다.");
                });
        String today = LocalDate.now().toString();
        dailyStatsRedisRepository.increment(today, "reports");
        reportRepository.save(new Report(type, targetId, reporter, dto.getReason()));
    }
}
