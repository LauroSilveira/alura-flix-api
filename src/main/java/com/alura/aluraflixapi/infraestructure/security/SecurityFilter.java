package com.alura.aluraflixapi.infraestructure.security;

import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Spring validate each request received only once
 */
@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final String PREFIX_LOGGING = "[SecurityFilter]";
    public static final String AUTHORIZATION = "Authorization";
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(final TokenService tokenService, final UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    //doFilterInternal is called for each request received
    //All request to endpoint/login don't have accessToken in theirs headers
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        log.info("{} request received intercepted by Internal Filter", PREFIX_LOGGING);
        final var tokenJWT = this.getTokenJWT(request);

        if (Objects.nonNull(tokenJWT)) {
            //Retrieve user from Token JWT
            final var username = this.tokenService.verifyTokenJWT(tokenJWT);
            final var user = this.userRepository.findByUsername(username);

            //after retrieve the user we need to tell a Spring framework to authenticate him in the context,
            //this is done by calling UsernamePasswordAuthenticationToken and SecurityContextHolder methods
            log.info("{} Authenticating user: {} ", PREFIX_LOGGING, user.getUsername());
            var authentication = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("{} User authenticated: {}", PREFIX_LOGGING, authentication.getPrincipal());
        }
        //continue the flow
        filterChain.doFilter(request, response);
    }

    private String getTokenJWT(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .map(token -> token.replace("Bearer", "").trim())
                .orElse(null);
    }
}
