package com.alura.aluraflixapi.infraestructure.security;


import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.infraestructure.exception.JwtRefreshTokenExpiredException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.api-issuer}")
    private String apiIssuer;

    @Value("${api.security.token-jwt-secret}")
    public String secret;

    public String generateTokenJwt(User user) {
        try {
            log.info("Generating Token JWT ...");
            return JWT.create()
                    .withIssuer(apiIssuer)
                    //owner
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getUsername())
                    .withClaim("role", user.getRoles()
                            .stream()
                            .map(Roles::getRole)
                            .map(Enum::name)
                            .toList())
                    // duration of JWT
                    .withExpiresAt(getExpireDate())
                    .sign(Algorithm.HMAC256(secret));

        } catch (NullPointerException exception) {
            throw new JWTCreationException("Error to create JWT accessToken", exception.getCause());
        }
    }

    public String verifyTokenJWT(String tokenJWT) {
        log.info("Verifying JWT accessToken ...");
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(apiIssuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Error verifying JWT Token", ex.getCause());
        }
    }

    public void isRefreshTokenExpired(String tokenJWT) {
        if (JWT.decode(tokenJWT).getExpiresAt().toInstant().compareTo(Instant.now()) < 0) {
            throw new JwtRefreshTokenExpiredException("Refresh token expired, please login again");
        }
    }

    //Create expire date of accessToken, in this case is the current hour plus 10 minutes
    private Instant getExpireDate() {
        return Instant.now().plus(1, ChronoUnit.MINUTES);

    }

    public String generateRefreshToken(String subject) {
        try {
            log.info("Generating Refresh Token JWT ...");
            return JWT.create()
                    .withIssuer(apiIssuer)
                    .withExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                    .withSubject(subject)
                    .sign(Algorithm.HMAC256(secret));
        } catch (RuntimeException exception) {
            throw new JWTCreationException("Error to create JWT refresh accessToken", exception.getCause());
        }
    }
}
