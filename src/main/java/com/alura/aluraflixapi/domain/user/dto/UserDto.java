package com.alura.aluraflixapi.domain.user.dto;

import com.alura.aluraflixapi.domain.user.roles.Roles;
import java.util.List;

public record UserDto(String id, String username, String password, List<Roles> roles) {

}
