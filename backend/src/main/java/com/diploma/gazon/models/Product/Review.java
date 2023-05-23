package com.diploma.gazon.models.Product;

import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Data
public class Review {
    @Id
    private String id;
    private Instant createdAt;
    private Float rating;
    private String body;
    @DBRef(lazy = true)
    private User user;

    public Review(Float rating, String body, User user) {
        this.id = new ObjectId().toString();
        this.createdAt = Instant.now();
        this.rating = rating;
        this.body = body;
        this.user = user;
    }

    public String getUserUsername() {
        return user.getUsername();
    }
}
