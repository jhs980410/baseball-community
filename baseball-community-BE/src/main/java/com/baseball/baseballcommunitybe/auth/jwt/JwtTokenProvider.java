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
    private final long ACCESS_EXP = 1000L * 60 * 15;   // 15분
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

    // Access Token 생성 (짧게)
    public String createAccessToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // userId를 subject로
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP)) // 15분
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // Refresh Token 생성 (길게)
    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP)) // 2주
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // 토큰 파싱
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    // 만료 여부 확인
    public boolean isExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // 남은 만료시간 조회
    public long getExpiration(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }
    //토큰꺼내기
    public String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 없으면 null 반환
    }
}
