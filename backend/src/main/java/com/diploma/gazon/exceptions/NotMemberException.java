package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class NotMemberException extends AppException {
    public NotMemberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
