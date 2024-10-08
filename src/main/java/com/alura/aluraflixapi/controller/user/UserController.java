package com.alura.aluraflixapi.controller.user;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  private static final String PREFIX_LOGGING = "[UserController]";
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
    log.info("{} Saving new User: {}", PREFIX_LOGGING, userDto.toString());
    var newUser = service.saveUser(userDto);
    return ResponseEntity.ok().body(newUser);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserDto>> getUsers() {
    log.info("{} Retrieving Users", PREFIX_LOGGING);
    var users = service.getUsers();
    return ResponseEntity.ok(users);
  }
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
    log.info("{} Request to delete user with id: {}", PREFIX_LOGGING, id);
    service.deleteUser(id);
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
