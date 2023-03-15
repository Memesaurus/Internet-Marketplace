package com.diploma.gazon.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class Review {
    @Id
    private String id;
    private Float rating;
    private String message;
    @DBRef
    private User author;
}
