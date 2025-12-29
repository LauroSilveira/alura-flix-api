package com.alura.aluraflixapi.domain.category.dto;

import com.alura.aluraflixapi.domain.category.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDto(String id,
                          @NotBlank
                          String rating,
                          @NotBlank
                          String title,
                          @NotBlank
                          String colorHex) {

    public CategoryDto(Category entity) {
        this(entity.getId(), entity.getRating(), entity.getTitle(), entity.getColorHex());
    }
}
