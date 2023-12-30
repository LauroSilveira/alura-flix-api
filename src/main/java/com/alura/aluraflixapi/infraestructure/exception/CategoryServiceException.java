package com.alura.aluraflixapi.infraestructure.exception;

public class CategoryServiceException extends RuntimeException {
    public CategoryServiceException(String message) {
        super(message);
    }

    public CategoryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
