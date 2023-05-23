package com.diploma.gazon.DTO.request;

import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.models.User.UserAddress;
import lombok.Data;

@Data
public class CartItemDTO {
    private ProductResponseDTO product;
    private Integer quantity;
}
