package com.example.car_insurance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResourceNotFoundException> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("date".equals(ex.getName())) {
            return ResponseEntity.badRequest()
                    .body(new ResourceNotFoundException("Invalid date format. Use YYYY-MM-DD"));
        }
        return ResponseEntity.badRequest()
                .body(new ResourceNotFoundException("Invalid request parameter: " + ex.getName()));
    }
}