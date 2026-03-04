package com.demoapp.demoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status,
            String message, List<String> details) {
        return ResponseEntity.status(status).body(Map.of("status", "error",
                "message", message, "details", details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed",
                errors);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(
            IOException e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "I/O error occurred", List.of(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationException(
            AuthorizationDeniedException e) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied",
                List.of(e.getMessage()));
    }

    @ExceptionHandler(NoSuchBucketException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchBucket(
            NoSuchBucketException e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "S3 bucket not found", List.of(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", List.of(e.getMessage()));
    }
}
