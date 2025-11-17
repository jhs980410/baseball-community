package com.baseball.baseballcommunitybe.user.service;

import com.baseball.baseballcommunitybe.user.dto.UserResponseDto;
import com.baseball.baseballcommunitybe.user.dto.UserUpdateRequestDto;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.exception.CustomErrorCode;
import com.baseball.baseballcommunitybe.user.exception.CustomException;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 특정 유저 조회
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponseDto(user);
    }

    // 유저 정보 수정
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

    // 유저 삭제 (탈퇴)
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new CustomException(CustomErrorCode.USER_ALREADY_DELETED);
        }

        // 1) Soft Delete 처리
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(User.Status.DELETED);

        // 2) 개인정보 파기 (DB 제약 만족하며 익명화)
        user.setEmail("DEL_" + user.getId() + "@deleted.local"); // NOT NULL + UNIQUE
        user.setNickname("탈퇴한 회원_" + user.getId()); // UNIQUE 깨지지 않도록 ID 포함

        // password NOT NULL → 더미 값으로 대체
        user.setPassword("DELETED_USER");

        user.setRefreshToken(null);

        // JPA Dirty Checking 자동 업데이트
    }



    // 관리자: 유저 상태 변경
    public void updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        try {
            user.setStatus(User.Status.valueOf(status.toUpperCase())); // 대소문자 구분 방지
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 상태 값입니다: " + status);
        }
        userRepository.save(user);
    }

    // 중복 체크
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
