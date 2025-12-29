package com.alura.aluraflixapi.infraestructure.service.user;

import com.alura.aluraflixapi.domain.user.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    UserDTO saveUser(UserDTO dto);

    List<UserDTO> getUsers();

    void deleteUser(String id);
}
