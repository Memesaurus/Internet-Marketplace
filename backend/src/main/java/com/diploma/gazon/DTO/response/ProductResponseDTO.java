package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Data
public class ProductResponseDTO {
    private String id;
    private String name;
    private String description;

    private Number price;
    private Boolean isInStock;

    private Float rating;
    private UserResponseDTO user;

    private Set<String> tags;
    private Set<String> images;

    private Set<ReviewResponseDTO> reviews;

}
