package com.diploma.gazon.DTO.request;

import com.diploma.gazon.models.User.UserAddress;
import lombok.Data;

import java.util.Map;

@Data
public class CartDTO {
    private Map<String, Integer> products;
    private UserAddress address;
}