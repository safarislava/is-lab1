package ru.ifmo.se.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.persistence.entity.RoleType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class JwtService {
    private static final String ISSUER = "is-lab";
    private static final String ROLES_CLAIM = "roles";
    private static final long ACCESS_TOKEN_VALIDITY_MINUTES = 15;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        String secret = Optional.ofNullable(System.getenv("JWT_SECRET"))
                .filter(s -> !s.isBlank())
                .orElseThrow(() -> new IllegalStateException("JWT_SECRET environment variable is not set"));
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
    }

    public String generateAccessToken(String username, Set<RoleType> roles) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ACCESS_TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES);

        List<String> roleNames = roles.stream()
                .map(RoleType::name)
                .toList();

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withClaim(ROLES_CLAIM, roleNames)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    public Optional<DecodedJWT> verifyToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }

    public String extractUsername(DecodedJWT decodedJwt) {
        return decodedJwt.getSubject();
    }

    public Set<String> extractRoles(DecodedJWT decodedJwt) {
        List<String> roles = decodedJwt.getClaim(ROLES_CLAIM).asList(String.class);
        if (roles == null) {
            return new HashSet<>();
        }
        return new HashSet<>(roles);
    }
}
