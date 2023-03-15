package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
