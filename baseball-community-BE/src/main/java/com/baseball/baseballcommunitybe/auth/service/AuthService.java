package com.baseball.baseballcommunitybe.auth.service;

import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.SignupRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.TokenResponseDto;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    // 비밀번호 정규식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * 회원가입
     */
    @Transactional
    public void signup(SignupRequestDto dto) {
        // 이메일/닉네임 중복 체크
        List<User> conflicts = userRepository.findByEmailOrNickname(dto.getEmail(), dto.getNickname());

        if (!pattern.matcher(dto.getPassword()).matches()) {
            throw new IllegalArgumentException("비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
        }

        for (User u : conflicts) {
            if (u.getEmail().equals(dto.getEmail())) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }
            if (u.getNickname().equals(dto.getNickname())) {
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
            }
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        userRepository.save(user);
    }

    /**
     * 로그인
     */
    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Access & Refresh Token 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // DB에 Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponseDto(accessToken, refreshToken, user.getId(), user.getEmail(), user.getNickname());
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(String accessToken) {
        long expiration = jwtTokenProvider.getExpiration(accessToken);

        // Access Token 블랙리스트 등록
        redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        // Refresh Token 제거
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        userRepository.findById(userId).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    /**
     * 토큰 재발급
     */
    public TokenResponseDto refresh(String refreshToken) {
        // 1. Refresh Token 유효성 체크
        if (refreshToken == null || jwtTokenProvider.isExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Refresh Token에서 userId 추출
        Long tokenUserId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 3. DB에서 유저 조회
        User user = userRepository.findById(tokenUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 4. DB에 저장된 refresh token과 비교 (본인 것인지 확인)
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token 불일치");
        }

        // 5. 새 Access Token & Refresh Token 발급
        String newAccess = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String newRefresh = jwtTokenProvider.createRefreshToken(user.getId());

        // 6. Refresh Token 갱신 (DB 업데이트)
        user.setRefreshToken(newRefresh);
        userRepository.save(user);

        return new TokenResponseDto(
                newAccess,
                newRefresh,
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }

    //비밀번호 검증
    public boolean verifyPassword(Long userId, String rawPassword) {
        return userRepository.findById(userId)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
    //토큰꺼내기

    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}