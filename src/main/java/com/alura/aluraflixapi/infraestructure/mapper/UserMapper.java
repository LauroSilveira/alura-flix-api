package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDTO;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "password", qualifiedByName = "encryptPassword")
  User mapToEntity(UserDTO dto);

  UserDTO mapToDto(User newUser);

  List<UserDTO> mapToUsersDto(List<User> users);


  @Named("encryptPassword")
  default String password(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }

}
