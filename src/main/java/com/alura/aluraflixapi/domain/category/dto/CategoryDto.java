package com.alura.aluraflixapi.domain.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(String id,
                          @NotBlank
                          String rating,
                          @NotBlank
                          String title,
                          @NotBlank
                          String colorHex) {

}
