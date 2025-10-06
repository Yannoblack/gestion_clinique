package com.clinique.yannic.gestion_clinique_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {

    @Value("${jwt.secret:change_me_secret}")
    private String secret;

    @Value("${jwt.issuer:gestion-clinique}")
    private String issuer;

    @Value("${jwt.expirationMinutes:120}")
    private long expirationMinutes;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .withSubject(subject)
                .sign(algorithm());
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm())
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}


