package com.alura.aluraflixapi.infraestructure.exception;

public class VideoServiceException extends RuntimeException {

    public VideoServiceException(String message) {
        super(message);
    }

    public VideoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
