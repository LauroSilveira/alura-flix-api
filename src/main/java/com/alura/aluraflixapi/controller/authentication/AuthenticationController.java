package com.alura.aluraflixapi.controller.authentication;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.AuthenticationDto;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.security.dto.TokenJwtDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid AuthenticationDto dto) {
        log.info("Request to Login user: {}", dto.username());
        // Object wrapper that contains password and user
        final var authenticationToken = new UsernamePasswordAuthenticationToken(dto.username(),
                dto.password());
        //authenticationManager compare the password of the request with the password from database.
        final var authentication = this.authenticationManager.authenticate(authenticationToken);
        final var tokenJWT = tokenService.generateTokenJWT((User) authentication.getPrincipal());
        log.info("Token Generated with Success!");
        return ResponseEntity.ok().body(new TokenJwtDto(tokenJWT));
    }

}
