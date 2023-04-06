package com.alura.aluraflixapi.infraestructure.security;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring validate each request received only once
 */
@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

  private static final String PREFIX_LOGGIN = "[SecurityFilter]";
  public static final String AUTHORIZATION = "Authorization";
  private final TokenService tokenService;

  private final UserRepository userRepository;

  public SecurityFilter(final TokenService tokenService, final UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  //doFilterInternal is called for each request received
  //All request to endpoint/login don´t have token in theirs headers
  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain) throws ServletException, IOException {

    log.info("{} request received intercepeted by Internal Filter", PREFIX_LOGGIN);
    final var tokenJWT = this.getTokenJWT(request);

    if (tokenJWT != null) {
      //Retrieve user from Token JWT
      final var subject = tokenService.getSubject(tokenJWT);
      var user = userRepository.findByUsername(subject);

      //after retrieve the user we need to tell to Spring framework to authenticate him in the context
      //this is done by calling UsernamePasswordAuthenticationToken and SecurityContextHolder methods
      log.info("{} Authenticating user: {} ", PREFIX_LOGGIN, user.getUsername());
      var authentication = new UsernamePasswordAuthenticationToken(user, null,
          user.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("{} User authenticated: {}", PREFIX_LOGGIN, authentication.getPrincipal());
    }
    filterChain.doFilter(request, response);
  }

  private String getTokenJWT(final HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION))
        .map(token -> token.replace("Bearer", "").trim())
        .orElse(null);
  }
}
