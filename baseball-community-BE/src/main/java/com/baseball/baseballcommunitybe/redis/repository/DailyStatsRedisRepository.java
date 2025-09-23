package com.baseball.baseballcommunitybe.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DailyStatsRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private String getKey(String date, String field) {
        return "daily:" + date + ":" + field; // 예: daily:2025-09-23:new_posts
    }

    public void increment(String date, String field) {
        redisTemplate.opsForValue().increment(getKey(date, field));
    }

    public int getCount(String date, String field) {
        String value = redisTemplate.opsForValue().get(getKey(date, field));
        return value == null ? 0 : Integer.parseInt(value);
    }

    public void clear(String date, String field) {
        redisTemplate.delete(getKey(date, field));
    }
}
