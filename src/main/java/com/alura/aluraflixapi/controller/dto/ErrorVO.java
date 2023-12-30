package com.alura.aluraflixapi.controller.dto;

import org.springframework.validation.FieldError;

public record ErrorVO(String field, String message) {

    public ErrorVO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
