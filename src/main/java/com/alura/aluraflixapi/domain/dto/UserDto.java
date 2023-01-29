package com.alura.aluraflixapi.domain.dto;

import com.alura.aluraflixapi.domain.roles.Roles;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserDto(
    @NotBlank
    String username,
    @NotBlank
    String password,
    List<Roles> roles) {

}
