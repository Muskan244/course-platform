package com.assignment.course_platform.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String issuer;

    private final Duration ttl;

    private final JwtEncoder jwtEncoder;

    public JwtService(String issuer, Duration ttl, JwtEncoder jwtEncoder) {
        this.issuer = issuer;
        this.ttl = ttl;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(final String email, final String role) {
        final var claimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuer(issuer)
                .expiresAt(Instant.now().plus(ttl))
                .claim("roles", List.of(role))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }
}
