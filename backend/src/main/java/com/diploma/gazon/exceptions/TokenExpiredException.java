package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends AppException {
    public TokenExpiredException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
