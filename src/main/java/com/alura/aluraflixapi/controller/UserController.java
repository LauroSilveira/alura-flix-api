package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.infraestructure.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  private static final String PREFIX_LOGGIN = "[UserController]";

  @Autowired
  private UserService service;

  @PostMapping
  @Secured("ROLE_ADMIN")
  public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
    log.info("{} Saving new User: {}", PREFIX_LOGGIN, userDto.toString());
    var newUser = this.service.saveUser(userDto);
    return ResponseEntity.ok().body(newUser);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    log.info("{} Retrieving Users", PREFIX_LOGGIN);
    var users = this.service.getUsers();
    return ResponseEntity.ok(users);
  }
}
