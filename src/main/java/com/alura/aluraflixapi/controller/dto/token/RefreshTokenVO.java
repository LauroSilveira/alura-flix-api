package com.alura.aluraflixapi.controller.dto.token;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenVO(@NotBlank String refreshToken) {
}
