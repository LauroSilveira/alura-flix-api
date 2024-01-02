package com.alura.aluraflixapi.controller.user;

import com.alura.aluraflixapi.infraestructure.exception.ErrorMessageVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthenticationController.class)
public class AuthenticationControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessageVO> handlerAuthenticationException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageVO(ex.getMessage(), HttpStatus.NOT_FOUND));
    }
}
