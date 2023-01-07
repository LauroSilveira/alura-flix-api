package com.alura.aluraflixapi.infraestructure.dto;

public record UpdateVideoDto(String id, String title, String Description, String url) {

  public UpdateVideoDto(VideoDto videoDto) {
    this(videoDto.id(), videoDto.title(), videoDto.description(), videoDto.description());
  }

}
