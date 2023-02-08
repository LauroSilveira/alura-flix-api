package com.alura.aluraflixapi.infraestructure.security;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring validate each request received only once
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {


  private TokenService tokenService;

  private UserRepository userRepository;

  public SecurityFilter(final TokenService tokenService, final UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  //doFilterInternal is called for each request received
  //All request to endpoint /login don´t have token in theirs headers
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
   final var tokenJWT = getTokenJWT(request);

   if(null != tokenJWT) {
     //Retrieve user from Token JWT
     final var subject = tokenService.getSubject(tokenJWT);
     var user = userRepository.findByUsername(subject);

     //after retriever user the User we need to tell to Spring framework to authenticate him in the context
     // this is done by calling UsernamePasswordAuthenticationToken and SecurityContextHolder methods
     var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
     SecurityContextHolder.getContext().setAuthentication(authentication);
   }

    filterChain.doFilter(request, response);
  }

  private String getTokenJWT(final HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("Authorization"))
        .map(token -> token.replace("Bearer", "").trim())
        .orElse(null);
  }
}
