package com.diploma.gazon.models.Product;

import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class Review {
    @Id
    private String id;
    private Float rating;
    private String body;
    @DBRef
    private Member author;

    public Review(Float rating, String body, Member author) {
        this.id = new ObjectId().toString();
        this.rating = rating;
        this.body = body;
        this.author = author;
    }

    public String getAuthorUsername() {
        return author.getUsername();
    }
}
