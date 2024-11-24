package com.alura.aluraflixapi.infraestructure.exception;

public class JwtRefreshTokenExpiredException extends RuntimeException {
    public JwtRefreshTokenExpiredException(String message) {
        super(message);
    }
}
