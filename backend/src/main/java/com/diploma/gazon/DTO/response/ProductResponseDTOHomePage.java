package com.diploma.gazon.DTO.response;

import lombok.Data;

import java.util.Set;

@Data
public class ProductResponseDTOHomePage {
    private String id;
    private String name;

    private Number price;
    private Boolean isInStock;

    private Float rating;
    private Integer reviewCount;
    private Set<String> images;
}