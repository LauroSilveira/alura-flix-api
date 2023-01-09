package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.domain.dto.UserDto;
import com.alura.aluraflixapi.infraestructure.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService service;

  @PostMapping
  public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
    var newUser = this.service.saveUser(userDto);
    return ResponseEntity.ok().body(newUser);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    var users = this.service.getUsers();
    return ResponseEntity.ok(users);
  }
}
