package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "password", qualifiedByName = "encryptPassword")
  User mapToEntity(UserDto dto);

  UserDto mapToDto(User newUser);

  List<UserDto> mapToUsersDto(List<User> users);


  @Named("encryptPassword")
  default String password(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }

}
