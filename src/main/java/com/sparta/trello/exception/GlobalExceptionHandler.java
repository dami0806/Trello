package com.sparta.trello.exception;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 사용자오류
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> UnauthorizedExceptionHandler(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}