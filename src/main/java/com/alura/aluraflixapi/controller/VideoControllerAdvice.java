package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.dto.ErrorDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VideoControllerAdvice {

  /**
   * Handle Invalid fields
   * @return List of ErrorDto with invalid fields
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorDto>> handleInvalidFields(final MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors();
    return ResponseEntity.badRequest().body(errors.stream().map(ErrorDto::new).toList());
  }

}
