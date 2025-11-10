package com.baseball.baseballcommunitybe.common.aop;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SuspendCheckAspect {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @CheckSuspended 어노테이션이 붙은 Controller 메서드 실행 전/후로 작동
     */
    @Around("@annotation(com.baseball.baseballcommunitybe.common.aop.CheckSuspended)")
    public Object checkSuspendedUser(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            log.warn("[정지검사] 인증 토큰이 없습니다. 요청 URI: {}", request.getRequestURI());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // DB에서 유저 상태 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (user.getStatus() == User.Status.SUSPENDED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "계정이 정지되었습니다. 관리자에게 문의하세요.");
        }

        // Redis 기반 임시 정지 여부 확인
        String key = "suspended:user:" + userId;
        String data = redisTemplate.opsForValue().get(key);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (data != null && ttl != null && ttl > 0) {
            String reason = "사유 없음";
            try {
                JsonNode node = objectMapper.readTree(data);
                reason = node.path("reason").asText("사유 없음");
            } catch (Exception ignored) {}

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    String.format("계정이 일시 정지되었습니다. (남은 시간: %d분, 사유: %s)", ttl / 60, reason)
            );
        }

        // 통과
        return joinPoint.proceed();
    }
}
