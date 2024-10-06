package com.alura.aluraflixapi.controller.authentication;

import com.alura.aluraflixapi.controller.dto.token.RefreshTokenVO;
import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.AuthenticationDto;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import com.alura.aluraflixapi.infraestructure.security.dto.TokenJwtDto;
import com.alura.aluraflixapi.infraestructure.service.token.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid AuthenticationDto dto) {
        log.info("Request to Login user: {}", dto.username());
        // Object wrapper that contains password and user
        final var authenticationToken = new UsernamePasswordAuthenticationToken(dto.username(),
                dto.password());
        //authenticationManager compare the password of the request with the password from database.
        final var authentication = this.authenticationManager.authenticate(authenticationToken);
        final var tokenJwt = tokenService.generateTokenJwt((User) authentication.getPrincipal());
        final var refreshToken = tokenService.generateRefreshToken(dto.username());
        log.info("Token Generated with Success!");
        return ResponseEntity.ok().body(new TokenJwtDto(tokenJwt, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenJwtDto> refreshAccessToken(@RequestBody RefreshTokenVO refreshToken) {
        log.info("Request to renew refresh accessToken");
        final var tokenJwtDto = refreshTokenService.refreshAccessToken(refreshToken.refreshToken());
        return ResponseEntity.ok(tokenJwtDto);
    }

}
