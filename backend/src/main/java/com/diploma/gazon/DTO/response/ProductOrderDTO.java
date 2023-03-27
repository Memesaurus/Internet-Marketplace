package com.diploma.gazon.DTO.response;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

@Data
public class ProductOrderDTO {
    private ProductResponseDTO product;
    private Integer quantity;
}
