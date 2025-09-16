package com.baseball.baseballcommunitybe.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class HotPostRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GLOBAL_KEY = "hot_posts";

    // 팀별 key 생성
    private String getTeamKey(Long teamId) {
        return "hot_posts:team:" + teamId;
    }

    // 점수 업데이트 (ZADD) → 전체 + 팀별
    public void updateScore(Long postId, Long teamId, double score) {
        redisTemplate.opsForZSet().add(GLOBAL_KEY, String.valueOf(postId), score);

        if (teamId != null) {
            String teamKey = getTeamKey(teamId);
            redisTemplate.opsForZSet().add(teamKey, String.valueOf(postId), score);
        }
    }

    // 상위 N개 조회 (전체 or 팀별)
    public List<Long> getTopPosts(Long teamId, int limit) {
        String key = (teamId != null) ? getTeamKey(teamId) : GLOBAL_KEY;

        Set<Object> result = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);
        if (result == null || result.isEmpty()) {
            return List.of();
        }
        return result.stream()
                .map(Object::toString)
                .map(Long::valueOf)
                .toList();
    }

    // 전체 삭제 (배치 리셋용) → 전체 + 팀별
    public void clear(Long teamId) {
        if (teamId == null) {
            redisTemplate.delete(GLOBAL_KEY);
        } else {
            redisTemplate.delete(getTeamKey(teamId));
        }
    }
}
