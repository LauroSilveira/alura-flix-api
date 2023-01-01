package com.alura.aluraflixapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record VideoDto(
    @NotBlank
    String id,
    @NotBlank
    String title,
    @NotBlank
    String description,
    @NotNull
    @URL
    String url) {


}
