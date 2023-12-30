package com.alura.aluraflixapi.domain.user.dto;

import com.alura.aluraflixapi.domain.user.roles.Roles;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserDto(
    String id,
    @NotBlank
    String username,
    @NotBlank
    String password,
    Set<Roles> roles) {

}
