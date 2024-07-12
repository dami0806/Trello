package com.sparta.trello.exception;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.card.exception.DatabaseAccessException;
import com.sparta.trello.domain.comment.exception.CommentNotFoundException;
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

    // 데이터 베이스 접근 실패
    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<String> DatabaseAccessExceptionHandler(DatabaseAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> CommentNotFoundExceptionHandler(CommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}