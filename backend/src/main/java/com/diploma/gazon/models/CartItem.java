package com.diploma.gazon.models;

import com.diploma.gazon.models.Product.Product;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CartItem {
    @DBRef(lazy = true)
    private Product product;
    private Integer quantity;
}
