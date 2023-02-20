package com.alura.aluraflixapi.infraestructure.exception;

public class CategoryTransactionException extends RuntimeException {

  public CategoryTransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
