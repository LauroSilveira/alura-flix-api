package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.dto.UserDto;
import com.alura.aluraflixapi.model.RoleEnum;
import com.alura.aluraflixapi.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody final UserDto dto){
    UserDto userDto = service.create(dto);
    return ResponseEntity.ok(userDto);
  }

  public ResponseEntity<List<UserDto>> getUsers() {
    List<UserDto> usersDto = service.getUsers();
    return ResponseEntity.ok(usersDto);
  }
}
