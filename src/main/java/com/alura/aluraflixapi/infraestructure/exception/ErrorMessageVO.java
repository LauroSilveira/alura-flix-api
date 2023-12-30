package com.alura.aluraflixapi.infraestructure.exception;

import org.springframework.http.HttpStatus;

public record ErrorMessageVO(String message, HttpStatus httpStatus) {
}
