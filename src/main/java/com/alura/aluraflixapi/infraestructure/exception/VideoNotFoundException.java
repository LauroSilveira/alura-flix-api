package com.alura.aluraflixapi.infraestructure.exception;

public class VideoNotFoundException extends RuntimeException{
    public VideoNotFoundException(String message) {
        super(message);
    }
}
