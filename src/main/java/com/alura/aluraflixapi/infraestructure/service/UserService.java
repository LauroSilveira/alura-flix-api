package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.infraestructure.mapper.UserMapper;
import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private UserRepository repository;

  private UserMapper mapper;


  public UserService(final UserRepository repository, final UserMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public UserDto create(UserDto dto) {
    final User user = mapper.mappToEntity(dto);
    final User newUser = repository.save(user);
    return mapper.mappToDto(newUser);
  }

  public List<UserDto> getUsers() {
    List<User> users = repository.findAll();
    return mapper.mappToUsersDto(users);
  }
}
