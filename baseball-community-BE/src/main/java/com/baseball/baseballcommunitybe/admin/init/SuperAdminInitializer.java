package com.baseball.baseballcommunitybe.init;

import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.email:super@system.com}")
    private String superAdminEmail;

    @Value("${app.super-admin.password:admin123!}")
    private String superAdminPassword;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(superAdminEmail)) {
            User superAdmin = User.builder()
                    .email(superAdminEmail)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .nickname("RootAdmin")
                    .role(User.Role.SUPER_ADMIN)
                    .status(User.Status.ACTIVE)
                    .build();

            userRepository.save(superAdmin);
            System.out.println("✅ SUPER_ADMIN 계정 생성 완료: " + superAdminEmail);
        } else {
            System.out.println("⚙️ SUPER_ADMIN 이미 존재함, 생성 스킵됨");
        }
    }
}
