package com.alura.aluraflixapi;

import com.alura.aluraflixapi.domain.user.dto.AuthenticationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private AuthenticationManager manager;

  @PostMapping
  public ResponseEntity login(@RequestBody @Valid AuthenticationDto dto) {
    final var token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
    final var atuhentication = this.manager.authenticate(token);
    return ResponseEntity.ok().build();
  }

}
