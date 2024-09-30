package com.alura.aluraflixapi.infraestructure.service.user;

import com.alura.aluraflixapi.domain.user.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    UserDto saveUser(UserDto dto);

    List<UserDto> getUsers();

    void deleteUser(String id);
}
