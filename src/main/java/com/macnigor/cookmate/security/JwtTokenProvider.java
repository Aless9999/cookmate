package com.macnigor.cookmate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key secretKey;

    private final long accessTokenValidityInMilliseconds = 3600000;        // 1 час
    private final long refreshTokenValidityInMilliseconds = 86400000L * 90; // 90 дней

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String createAccessToken(String username) {
        return createToken(username, accessTokenValidityInMilliseconds, "access");
    }

    public String createRefreshToken(String username) {
        return createToken(username, refreshTokenValidityInMilliseconds, "refresh");
    }

    private String createToken(String username, long validityMillis, String type) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("token_type", type)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, "access");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, "refresh");
    }

    private boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = parseToken(token);
            String actualType = (String) claims.get("token_type");
            return expectedType.equals(actualType);
        } catch (JwtException | IllegalArgumentException e) {
            // log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
