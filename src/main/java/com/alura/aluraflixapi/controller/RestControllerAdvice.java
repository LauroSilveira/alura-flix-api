package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.dto.ErrorDto;
import com.alura.aluraflixapi.infraestructure.exception.AuthenticationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

  /**
   * Handle Invalid fields
   * @return List of ErrorDto with invalid fields
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorDto>> handleInvalidFields(final MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors();
    return ResponseEntity.badRequest().body(errors.stream().map(ErrorDto::new).toList());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<AuthenticationException> handleAuthenticationException(final AuthenticationException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthenticationException(
        exception.getMessage()));
  }

}
