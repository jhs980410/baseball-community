package com.baseball.baseballcommunitybe.auth.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            System.out.println("JwtAuthenticationFilter 실행됨, token=" + token);
            if (token != null && !jwtTokenProvider.isExpired(token)) {
                Claims claims = jwtTokenProvider.parseToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                String role = (String) claims.get("role");

                // Spring Security 인증 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId, // principal
                        null,   // credentials
                        Collections.emptyList() // 권한 (필요 시 ROLE_USER 등으로 매핑)
                );
                ((UsernamePasswordAuthenticationToken) authentication)
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("JWT 인증 실패", e);
        }

        filterChain.doFilter(request, response);
    }
}
