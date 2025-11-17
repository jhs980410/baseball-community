package com.baseball.baseballcommunitybe.auth.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    // 토큰 만료시간
    private final long ACCESS_EXP = 1000L * 60 * 15;            // 15분
    private final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14; // 2주

    public JwtTokenProvider() throws Exception {
        // ====== ClassPath 기반으로 키 로딩 (jar/EC2/Docker 모두 지원) ======
        String privateKeyContent = new ClassPathResource("keys/private.pem")
                .getContentAsString(StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] privateDecoded = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateDecoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(privateSpec);

        String publicKeyContent = new ClassPathResource("keys/public.pem")
                .getContentAsString(StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] publicDecoded = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicDecoded);
        this.publicKey = keyFactory.generatePublic(publicSpec);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // 토큰 파싱
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    public String getRoleFromToken(String token) {
        return (String) parseToken(token).get("role");
    }

    public String getTokenType(String token) {
        return (String) parseToken(token).get("type");
    }

    public boolean isExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public long getExpiration(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    // Authorization 헤더 및 쿠키에서 JWT 추출
    public String resolveToken(HttpServletRequest request) {

        // 1) 쿠키에서 ACCESS_TOKEN 또는 ADMIN_TOKEN 먼저 찾기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            for (Cookie cookie : request.getCookies()) {
                if ("ADMIN_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 2) Authorization 헤더 (fallback)
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }
}
