package com.baseball.baseballcommunitybe.admin.scheduler;

import com.baseball.baseballcommunitybe.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SuspendScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final AdminUserService adminUserService;

    @Scheduled(fixedRate = 60 * 60 * 1000) // 1시간마다 검사
    public void checkSuspendedUsers() {
        Set<String> keys = redisTemplate.keys("suspended:user:*");
        if (keys == null) return;

        for (String key : keys) {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (expire != null && expire <= 0) {
                Long userId = Long.parseLong(key.split(":")[2]);
                adminUserService.restoreUser(userId);
                redisTemplate.delete(key);
            }
        }
    }
}
