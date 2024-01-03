package com.alura.aluraflixapi.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserDto(
    String id,
    @NotBlank
    String username,
    @NotBlank
    String password,
    Set<RolesDto> roles) {

}
