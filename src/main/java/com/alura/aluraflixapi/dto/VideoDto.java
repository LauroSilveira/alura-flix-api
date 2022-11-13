package com.alura.aluraflixapi.dto;

import lombok.Builder;


@Builder
public record VideoDto(String id, String title, String description, String url) {

}
