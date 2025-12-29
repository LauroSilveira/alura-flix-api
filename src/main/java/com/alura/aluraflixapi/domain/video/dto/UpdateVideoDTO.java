package com.alura.aluraflixapi.domain.video.dto;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;

public record UpdateVideoDTO(String id, String title, String description, String url, CategoryDto category) {

}
