package com.baseball.baseballcommunitybe.user.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.user.dto.UserResponseDto;
import com.baseball.baseballcommunitybe.user.dto.UserUpdateRequestDto;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // JWT 파싱 유틸 (userId 꺼내기용)

    // 내 정보 조회 (id 기준)
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponseDto(user);
    }

    // 회원 정보 수정 (id 기준)
    public void updateUser(Long id, UserUpdateRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
            user.setNickname(dto.getNickname());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ------------------ 쿠키 기반 ------------------

    // 내 정보 조회 (쿠키에서 userId 추출)
    public UserResponseDto getMyProfile(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        return getUser(userId);
    }

    // 내 정보 수정
    public void updateMyProfile(HttpServletRequest request, UserUpdateRequestDto dto) {
        Long userId = extractUserIdFromRequest(request);
        updateUser(userId, dto);
    }

    // 내 계정 삭제
    public void deleteMyAccount(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        userRepository.deleteById(userId);
    }

    // 관리자: 유저 상태 변경
    public void updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setStatus(User.Status.valueOf(status));
        userRepository.save(user);
    }

    // ------------------ 내부 헬퍼 ------------------

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new IllegalArgumentException("인증 토큰이 없습니다.");
        }

        return jwtTokenProvider.getUserIdFromToken(token); // JWT 파싱해서 userId 추출
    }
}
