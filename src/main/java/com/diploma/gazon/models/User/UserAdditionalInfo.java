package com.diploma.gazon.models.User;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAdditionalInfo implements Serializable {
    private UserAddress address;
    private String phone;
}
