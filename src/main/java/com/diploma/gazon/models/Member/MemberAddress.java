package com.diploma.gazon.models.Member;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberAddress {
    private String country;
    private String city;
    private String street;
    private String house;
}
