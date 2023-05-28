package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  UserDto saveUser(UserDto dto);

  List<UserDto> getUsers();
}
