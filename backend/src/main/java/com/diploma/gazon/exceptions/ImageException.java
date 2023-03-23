package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class ImageException extends AppException {
    public ImageException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
