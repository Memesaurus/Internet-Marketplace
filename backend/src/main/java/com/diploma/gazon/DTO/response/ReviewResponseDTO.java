package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class ReviewResponseDTO {
    private String id;
    private Float rating;
    private String body;
    private UserResponseDTO author;
}
