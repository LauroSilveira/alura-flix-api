package com.alura.aluraflixapi.infraestructure.security;


import com.alura.aluraflixapi.domain.roles.Roles;
import com.alura.aluraflixapi.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  public static final String ALURA_FLIX_API = "alura-flix-api";

  @Value("${api.security.token-jwt-secret}")
  public String secret;

  public String generateTokenJWT(User user) {
    try {

      return JWT.create()
          .withIssuer(ALURA_FLIX_API)
          //owner
          .withSubject(user.getUsername())
          .withClaim("id", user.getUsername())
          .withClaim("role", user.getRoles().stream().map(Roles::getRole).toList())
          // duration of JWT
          .withExpiresAt(getExpireDate())
          .sign(Algorithm.HMAC256(secret));

    } catch (JWTCreationException exception) {
      throw new JWTCreationException("Error to create JWT token", exception.getCause());
    }
  }

  private Instant getExpireDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);

  }
}
