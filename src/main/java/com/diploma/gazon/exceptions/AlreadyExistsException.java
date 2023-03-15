package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends AppException {
    public AlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
