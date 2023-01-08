package com.alura.aluraflixapi.infraestructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record VideoDto(
    String id,
    @NotBlank
    String title,
    @NotBlank
    String description,
    @NotNull
    @URL
    String url) {


}
