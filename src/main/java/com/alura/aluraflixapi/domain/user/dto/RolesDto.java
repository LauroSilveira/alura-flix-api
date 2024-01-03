package com.alura.aluraflixapi.domain.user.dto;

import com.alura.aluraflixapi.domain.user.roles.RolesEnum;

public record RolesDto(String id,
                       RolesEnum role) {
}
