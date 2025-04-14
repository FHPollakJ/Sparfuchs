package com.sparfuchs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logger.warn("NotFoundException: {} - Request: {}", ex.getMessage(), request.getDescription(false));
        return buildResponseEntity("File not found. ", HttpStatus.NOT_FOUND);
    }

    // 400 - Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        logger.warn("BadRequestException: {} - Request: {}", ex.getMessage(), request.getDescription(false));
        return buildResponseEntity("Bad request. "+ ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 403 - Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        logger.warn("ForbiddenException: {} - Request: {}", ex.getMessage(), request.getDescription(false));
        return buildResponseEntity("Access forbidden. " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // 500 - Internal Server Error
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseException(org.springframework.dao.DataAccessException ex, WebRequest request) {
        logger.error("DatabaseException: {} - Request: {}", ex.getMessage(), request.getDescription(false));
        return buildResponseEntity("Database exception. ", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled Exception: {} - Request: {}", ex.getMessage(), request.getDescription(false));
        return buildResponseEntity("Server died. "+ ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}

