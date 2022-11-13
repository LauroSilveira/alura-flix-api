package com.alura.aluraflixapi.mapper;

import com.alura.aluraflixapi.model.User;
import com.alura.aluraflixapi.dto.UserDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User mappToEntity(UserDto dto);

  UserDto mappToDto(User newUser);

  List<UserDto> mappToUsersDto(List<User> users);
}
