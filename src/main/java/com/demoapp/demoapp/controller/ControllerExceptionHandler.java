package com.demoapp.demoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.io.IOException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles validation exceptions.
     * @param e the {@link MethodArgumentNotValidException exception}
     * @return {@link ResponseEntity} with error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handles I/O exceptions.
     * @param e the {@link IOException exception}
     * @return {@link ResponseEntity} with error message
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    /**
     * Handles authorization denied exceptions.
     * @param e the {@link AuthorizationDeniedException exception}
     * @return {@link ResponseEntity} with error message
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * Handles NoSuchBucket exceptions.
     * @param e the {@link NoSuchBucketException exception}
     * @return {@link ResponseEntity} with error message
     */
    @ExceptionHandler(NoSuchBucketException.class)
    public ResponseEntity<String> handleNoSuchBucket(NoSuchBucketException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * Handles generic exceptions.
     * @param e the {@link Exception exception}
     * @return {@link ResponseEntity} with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
