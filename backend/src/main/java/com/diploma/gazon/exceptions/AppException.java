package com.diploma.gazon.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus httpStatus;

    public AppException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
