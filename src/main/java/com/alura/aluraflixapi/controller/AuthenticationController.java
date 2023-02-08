package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.dto.AuthenticationDto;
import com.alura.aluraflixapi.infraestructure.security.dto.TokenJwtDto;
import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.infraestructure.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {


  private final AuthenticationManager manager;


  private TokenService tokenService;

  public AuthenticationController(AuthenticationManager manager, TokenService tokenService) {
    this.manager = manager;
    this.tokenService = tokenService;
  }

  @PostMapping
  public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid AuthenticationDto dto) {
    final var authenticationToken = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
    final var authentication = this.manager.authenticate(authenticationToken);

    final var tokenJWT = tokenService.generateTokenJWT(
        (User) authentication.getPrincipal());
    return ResponseEntity.ok().body(new TokenJwtDto(tokenJWT));
  }

}
