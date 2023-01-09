package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User mappToEntity(UserDto dto);

  UserDto mappToDto(User newUser);

  List<UserDto> mappToUsersDto(List<User> users);
}
