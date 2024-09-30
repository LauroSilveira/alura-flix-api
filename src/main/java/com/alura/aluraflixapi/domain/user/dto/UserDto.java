package com.alura.aluraflixapi.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserDto(
    String id,
    @NotBlank
    String username,
    @NotBlank
    String password,
    @NotEmpty
    Set<RolesDto> roles) {

}
