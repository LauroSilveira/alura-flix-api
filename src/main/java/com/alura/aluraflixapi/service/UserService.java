package com.alura.aluraflixapi.service;

import com.alura.aluraflixapi.dto.UserDto;
import com.alura.aluraflixapi.mapper.UserMapper;
import com.alura.aluraflixapi.model.User;
import com.alura.aluraflixapi.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  public UserDetails findUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByusername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
  }
}
