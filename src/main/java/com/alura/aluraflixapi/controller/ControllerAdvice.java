package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.dto.ErrorDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  /**
   * Handle Invalid fields
   * @return List of ErrorDto with invalid fields
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorDto>> handleInvalidFields(
      final MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors();
    return ResponseEntity.badRequest().body(errors.stream().map(ErrorDto::new).toList());
  }

  /**
   * handle invalid credentials whe user atempt to login
   * @param ex HttpMessageNotReadableException
   * @return ResponseEntity status bad_request
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleLoginException(
      final HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest().body("Invalid Credentials");
  }

}
