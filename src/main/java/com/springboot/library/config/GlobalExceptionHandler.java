package com.springboot.library.config;

import com.springboot.library.dto.response.ErrorRespDto;
import com.springboot.library.exception.DuplicateResourceException;
import com.springboot.library.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.springboot.library.exception.InvalidCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger("GlobalExceptionHandler.class");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorRespDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("Resource not found in DB");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorRespDto(e.getMessage(),
                        "Resource is not in DB",
                        Instant.now()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorRespDto> handleDuplicateResourceException(DuplicateResourceException e) {
        log.warn("Duplicate resource");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorRespDto(e.getMessage(),
                        "Resource already exists",
                        Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        Map<String, String> map = new HashMap<>();
        List<FieldError> list = e.getBindingResult().getFieldErrors();
        list.forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
            log.warn("Field {} , failed validation: {}", fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(InvalidCallException.class)
    public ResponseEntity<ErrorRespDto> handleInvalidCallException(InvalidCallException e) {
        return ResponseEntity.badRequest().body(
                new ErrorRespDto(e.getMessage(),
                        "Call is Invalid due to malformed data",
                        Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespDto> handleException(Exception e) {
        return ResponseEntity.badRequest().body(
                new ErrorRespDto(e.getMessage(),
                        "Invalid data passed",
                        Instant.now()));
    }
}