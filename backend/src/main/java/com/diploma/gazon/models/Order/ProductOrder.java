package com.diploma.gazon.models.Order;

import com.diploma.gazon.models.Product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder implements Serializable {
    @DBRef
    private Product product;
    private Integer quantity;
}
