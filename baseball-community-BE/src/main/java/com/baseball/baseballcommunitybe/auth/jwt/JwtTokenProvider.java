package com.baseball.baseballcommunitybe.auth.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
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
    private final long ACCESS_EXP = 1000L * 60 * 15;           // 15분
    private final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14; // 2주

    public JwtTokenProvider() throws Exception {
        // src/main/resources/keys/private.pem, public.pem 불러오기
        String privateKeyContent = Files.readString(Paths.get("src/main/resources/keys/private.pem"))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(keySpec);

        String publicKeyContent = Files.readString(Paths.get("src/main/resources/keys/public.pem"))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] publicKeyDecoded = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyDecoded);
        this.publicKey = keyFactory.generatePublic(pubKeySpec);
    }

    //  Access Token 생성 (role 포함)
    public String createAccessToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // userId → subject
                .claim("role", role)
                .claim("type", "access")               // 구분을 위해 type 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    //  Refresh Token 생성 (role 불필요, 최소 정보만)
    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("type", "refresh")              // 구분을 위해 type 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    //  토큰 파싱 (예외 처리 강화)
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e; // 만료된 토큰 → 따로 처리
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    //  토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    //  토큰에서 role 추출
    public String getRoleFromToken(String token) {
        return (String) parseToken(token).get("role");
    }

    // 토큰 타입 추출 (access / refresh)
    public String getTokenType(String token) {
        return (String) parseToken(token).get("type");
    }

    //  만료 여부 확인
    public boolean isExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    //  남은 만료시간 조회
    public long getExpiration(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    //  토큰 추출 (Authorization 헤더 + 쿠키 둘 다 지원)
    public String resolveToken(HttpServletRequest request) {
        System.out.println("resolveToken 실행됨");

        // 1. Authorization 헤더에서 토큰 확인
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            System.out.println("Authorization 헤더에서 토큰 추출됨");
            return bearer.substring(7);
        }

        // 2. 쿠키에서 토큰 확인 (ACCESS_TOKEN, ADMIN_TOKEN 순서)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String name = cookie.getName();
                if ("ADMIN_TOKEN".equals(name)) {
                    System.out.println("ADMIN_TOKEN 쿠키 발견됨");
                    return cookie.getValue();
                }
            }
            for (Cookie cookie : request.getCookies()) {
                String name = cookie.getName();
                if ("ACCESS_TOKEN".equals(name)) {
                    System.out.println("ACCESS_TOKEN 쿠키 발견됨");
                    return cookie.getValue();
                }
            }
        }

        System.out.println("토큰을 찾을 수 없음");
        return null;
    }

}
