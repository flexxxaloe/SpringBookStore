package me.dev.springbookstore.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;

    private final Long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration
    ) {
        this.key = Keys.hmacShaKeyFor( // Один и тот же ключ используется и для подписи, и для проверки.
                secret.getBytes(StandardCharsets.UTF_8)
        );
        this.expiration = expiration;
    }

    public String generateToken(@NonNull String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + getExpiration()))
                .id(UUID.randomUUID().toString())
                .signWith(getKey())
                .compact();
        //.claim("role", role) и можно было бы String role передать (например)
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public final SecretKey getKey() {
        return key;
    }
    public final Long getExpiration() {
        return expiration;
    }
}
