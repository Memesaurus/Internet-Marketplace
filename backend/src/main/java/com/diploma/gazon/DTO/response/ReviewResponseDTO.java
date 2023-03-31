package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Data
public class ReviewResponseDTO {
    private String id;
    private Instant createdAt;
    private Float rating;
    private String body;
    private UserResponseDTO user;
}
