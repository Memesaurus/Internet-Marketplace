package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class OrderException extends AppException {
    public OrderException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
