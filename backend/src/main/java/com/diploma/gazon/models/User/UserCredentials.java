package com.diploma.gazon.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentials {
    private String username;
    private String password;
    private String email;
}
