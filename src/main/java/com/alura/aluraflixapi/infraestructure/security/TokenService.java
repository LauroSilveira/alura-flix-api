package com.alura.aluraflixapi.infraestructure.security;


import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

  public static final String ALURA_FLIX_API = "alura-flix-api";

  @Value("${api.security.token-jwt-secret}")
  public String secret;

  public String generateTokenJWT(User user) {
    try {
      log.info("Generating Token JWT ...");
      return JWT.create()
          .withIssuer(ALURA_FLIX_API)
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
      throw new JWTCreationException("Error to create JWT token", exception.getCause());
    }
  }

  public String getSubject(String tokenJWT) {
    try {
      var algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer(ALURA_FLIX_API)
          .build()
          .verify(tokenJWT)
          .getSubject();
    } catch (JWTDecodeException ex) {
      throw new JWTDecodeException("Error verifying JWT Token", ex.getCause());
    }

  }
  //Create expire date of token, in this case is the current hour plus 2 hours
  private Instant getExpireDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);

  }
}
