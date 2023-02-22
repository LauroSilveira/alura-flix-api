package com.alura.aluraflixapi.domain.video.dto;

import com.alura.aluraflixapi.domain.category.dto.CategoryDto;

public record UpdateVideoDto(String id, String title, String description, String url, CategoryDto category) {

  public UpdateVideoDto(VideoDto videoDto) {
    this(videoDto.id(), videoDto.title(), videoDto.description(), videoDto.description(), videoDto.category());
  }

}
