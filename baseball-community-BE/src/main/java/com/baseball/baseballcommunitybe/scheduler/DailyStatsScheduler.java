package com.baseball.baseballcommunitybe.scheduler;

import com.baseball.baseballcommunitybe.admin.entity.AdminDailyStats;
import com.baseball.baseballcommunitybe.redis.repository.DailyStatsRedisRepository;
import com.baseball.baseballcommunitybe.admin.repository.AdminDailyStatsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyStatsScheduler {

    private final DailyStatsRedisRepository redisRepository;
    private final AdminDailyStatsRepository dailyStatsRepository;

    @Scheduled(cron = "0 5 0 * * *") // 매일 00:05 실행
    public void flushDailyStats() {
        String yesterday = LocalDate.now().minusDays(1).toString(); // YYYY-MM-DD

        int newUsers = redisRepository.getCount(yesterday, "new_users");
        int newPosts = redisRepository.getCount(yesterday, "new_posts");
        int newComments = redisRepository.getCount(yesterday, "new_comments");
        int reports = redisRepository.getCount(yesterday, "reports");

        // DB에 INSERT
        AdminDailyStats stats = AdminDailyStats.builder()
                .statDate(LocalDate.parse(yesterday))
                .newUsers(newUsers)
                .newPosts(newPosts)
                .newComments(newComments)
                .reports(reports)
                .build();

        dailyStatsRepository.save(stats);

        // Redis 초기화
        redisRepository.clear(yesterday, "new_users");
        redisRepository.clear(yesterday, "new_posts");
        redisRepository.clear(yesterday, "new_comments");
        redisRepository.clear(yesterday, "reports");
    }
}
