package com.diploma.gazon.models.User;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddress implements Serializable {
    private String country;
    private String city;
    private String street;
    private String house;

    @Override
    public String toString() {
        return "Страна " + country + " город " + city + " улица " + street + " дом " + house;
    }
}
