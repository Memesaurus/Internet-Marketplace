package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class PhotoLimitExceededException extends AppException{
    public PhotoLimitExceededException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
