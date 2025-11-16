package com.baseball.baseballcommunitybe.auth;

import com.baseball.baseballcommunitybe.auth.jwt.JwtAuthenticationFilter;
import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173",
                            "http://localhost:5174",

                            "https://baseballjhs.o-r.kr",
                            "https://baseballjhs.o-r.kr:*",

                            // ★ 관리자 페이지 도메인 추가
                            "https://adminbaseball.o-r.kr",
                            "https://adminbaseball.o-r.kr:*"
                    ));

                    config.setAllowedMethods(List.of(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
                    ));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);

                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/check-email").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/check-nickname").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/notices/**").permitAll()

                        .requestMatchers("/api/super/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")

                        .requestMatchers("/api/posts/**").authenticated()
                        .requestMatchers("/api/comments/**").authenticated()
                        .requestMatchers("/api/likes/**").authenticated()
                        .requestMatchers("/api/reports/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
