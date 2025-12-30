package com.alura.aluraflixapi.infraestructure.service.user;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDTO;
import com.alura.aluraflixapi.infraestructure.mapper.UserMapper;
import com.alura.aluraflixapi.infraestructure.repository.RoleRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  private final RoleRepository roleRepository;

  private final UserMapper mapper;

  @Override
  public UserDTO saveUser(UserDTO dto) {
    log.info("Saving user: {}", dto);
    final User user = mapper.mapToEntity(dto);
    //first save Document Roles
    this.roleRepository.saveAll(user.getRoles());
    //After save Document User
    final User newUser = repository.save(user);
    return mapper.mapToDto(newUser);
  }

  @Override
  public List<UserDTO> getUsers() {
    log.info("Get all users");
    List<User> users = repository.findAll();
    return mapper.mapToUsersDto(users);
  }

  @Override
  public void deleteUser(String id) {
    log.info("Deleting user:  {}", id);
    repository.deleteById(id);
  }
}
