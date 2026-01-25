/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenFactory {




    private static final long ACCESS_TOKEN_TTL = 60 * 60 * 1000L; // 1 час
    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS = "access";

    private final Key secretKey;


    public JwtTokenFactory(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

    }

    public String createAccessToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_TTL);

        return Jwts.builder()
                .setSubject(username)
                .claim(TOKEN_TYPE, ACCESS)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }
}

