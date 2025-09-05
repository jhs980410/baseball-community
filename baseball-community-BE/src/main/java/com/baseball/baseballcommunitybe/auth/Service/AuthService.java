package com.baseball.baseballcommunitybe.auth.Service;


import com.baseball.baseballcommunitybe.auth.dto.LoginRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.SignupRequestDto;
import com.baseball.baseballcommunitybe.auth.dto.TokenResponse;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    //비밀번호 정규식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    // 회원가입
    @Transactional
    public void signup(SignupRequestDto dto) {
        // 이메일 또는 닉네임 중복 체크 (쿼리 1번)
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

        // 비밀번호 암호화 후 유저 생성
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        userRepository.save(user);
    }


    // 로그인
    public TokenResponse login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        return new TokenResponse(token,user.getId(), user.getEmail(), user.getNickname());
    }
}
