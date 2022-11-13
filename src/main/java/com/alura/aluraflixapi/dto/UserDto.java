package com.alura.aluraflixapi.dto;

import com.alura.aluraflixapi.model.Roles;
import java.util.List;

public record UserDto(String id, String username, String password, List<Roles> roles) {

}
