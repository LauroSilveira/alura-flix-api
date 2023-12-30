package com.alura.aluraflixapi.domain.video.dto;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record VideoDto(
    String id,
    @NotBlank
    String title,
    @NotBlank
    String description,
    @NotNull
    @URL
    String url,
    @NotNull
    CategoryDto category) {


}
