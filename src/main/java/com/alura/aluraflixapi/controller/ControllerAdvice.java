package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.dto.ErrorVO;
import com.alura.aluraflixapi.infraestructure.exception.ErrorMessageVO;
import com.alura.aluraflixapi.infraestructure.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Handle Invalid fields
     *
     * @return List of ErrorDto with invalid fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorVO>> handleInvalidFields(
            final MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ErrorVO::new).toList());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageVO> handlerResourceNotFoundException(final ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageVO(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

}
