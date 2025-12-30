package com.alura.aluraflixapi.controller.user;

import com.alura.aluraflixapi.domain.user.dto.UserDTO;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDto) {
    var newUser = service.saveUser(userDto);
    return ResponseEntity.ok().body(newUser);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserDTO>> getUsers() {
    var users = service.getUsers();
    return ResponseEntity.ok(users);
  }
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable final String id) {
    service.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
