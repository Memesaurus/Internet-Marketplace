package com.diploma.gazon.DTO.request;

import lombok.Data;

@Data
public class AuthDTO {
    private String username;
    private String password;
    private Boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }
}
