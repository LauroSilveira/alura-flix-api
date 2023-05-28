package com.alura.aluraflixapi.domain.user.dto;

import com.alura.aluraflixapi.domain.user.roles.Roles;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserDto(
    String id,
    @NotBlank
    String username,
    @NotBlank
    String password,
    List<Roles> roles) {

}
