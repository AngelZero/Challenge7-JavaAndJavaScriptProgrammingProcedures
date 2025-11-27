package com.bookingmx.reservations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

/**
 * Global exception handler mapping domain exceptions to HTTP responses.
 * - BadRequestException → 400
 * - NotFoundException  → 404
 * - Exception          → 500 (generic)
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(ex.getMessage(), 400));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(ex.getMessage(), 404));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody("Unexpected error", 500));
    }

    private Map<String, Object> errorBody(String message, int status) {
        return Map.of("timestamp", Instant.now().toString(), "status", status, "message", message);
    }
}
