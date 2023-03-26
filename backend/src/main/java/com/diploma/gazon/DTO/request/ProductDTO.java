package com.diploma.gazon.DTO.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private Number price;
    private Set<String> tags;
}
