package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException {
    public UnauthorizedException() {
        super(HttpStatus.FORBIDDEN, "Unauthorized");
    }
}
