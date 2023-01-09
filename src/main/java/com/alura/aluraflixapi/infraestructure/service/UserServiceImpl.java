package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.infraestructure.mapper.UserMapper;
import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.infraestructure.repository.RoleRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  private final RoleRepository roleRepository;

  private final UserMapper mapper;


  public UserServiceImpl(final UserRepository repository, final RoleRepository roleRepository, final UserMapper mapper) {
    this.repository = repository;
    this.roleRepository = roleRepository;
    this.mapper = mapper;
  }

  @Override
  public UserDto saveUser(UserDto dto) {
    final User user = mapper.mappToEntity(dto);
    //first save Document Roles
    this.roleRepository.saveAll(dto.roles());
    //After save Document User
    final User newUser = repository.save(user);
    return mapper.mappToDto(newUser);
  }

  @Override
  public List<UserDto> getUsers() {
    List<User> users = repository.findAll();
    return mapper.mappToUsersDto(users);
  }
}
