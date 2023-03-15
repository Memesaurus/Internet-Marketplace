package com.diploma.gazon.controllers;

import com.diploma.gazon.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler
    public ResponseEntity<String> catchAppException(AppException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
