package org.example.demo3.web;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionMessage(
                        "Goods Not Found",
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler({
            IllegalStateException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(
                        "Bad Request",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
