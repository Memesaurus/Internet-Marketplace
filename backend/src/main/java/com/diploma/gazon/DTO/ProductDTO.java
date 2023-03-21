package com.diploma.gazon.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> tags;
}
