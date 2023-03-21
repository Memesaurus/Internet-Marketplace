package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class NotCompanyException extends AppException {
    public NotCompanyException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
