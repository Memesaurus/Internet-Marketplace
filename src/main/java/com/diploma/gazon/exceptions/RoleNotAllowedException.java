package com.diploma.gazon.exceptions;

import org.springframework.http.HttpStatus;

public class RoleNotAllowedException extends AppException{

    public RoleNotAllowedException() {
        super(HttpStatus.BAD_REQUEST, "Cannot create a user of given role");
    }
}
