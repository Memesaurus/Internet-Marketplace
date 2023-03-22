package com.diploma.gazon.models.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ProductImage implements Serializable {
    private String id;
    private Binary imageData;

    public ProductImage(Binary imageData) {
        this.id = new ObjectId().toString();
        this.imageData = imageData;
    }
}
