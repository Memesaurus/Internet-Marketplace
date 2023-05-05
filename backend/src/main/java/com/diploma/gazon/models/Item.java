package com.diploma.gazon.models;

import com.diploma.gazon.models.Product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Item {
    @DBRef(lazy = true)
    private Product product;
    private Integer quantity;
}
