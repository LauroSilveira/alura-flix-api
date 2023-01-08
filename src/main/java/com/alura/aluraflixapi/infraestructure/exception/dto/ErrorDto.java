package com.alura.aluraflixapi.infraestructure.exception.dto;

import lombok.Builder;
import org.springframework.validation.FieldError;

public record ErrorDto(String field, String message) {

  public ErrorDto(FieldError fieldError) {
    this(fieldError.getField(), fieldError.getDefaultMessage());
  }

}
