package com.diploma.gazon.models.Product;

import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.models.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal stock;
    private Float rating;
    @DBRef
    private User owner;
    private List<Review> reviews;
    private List<String> tags;

    public synchronized void addReview(Review review) {
        this.reviews.add(review);
        calculateRating();
    }

    public synchronized void calculateRating() {
        Float ratingSum = reviews.stream()
                .map(Review::getRating)
                .reduce(Float::sum)
                .orElse(0F);

        this.rating = ratingSum / reviews.size();
    }

    public Review getReviewByUsername(String username) {
        return reviews.stream()
                .filter(o -> Objects.equals(o.getAuthor().getUsername(), username))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public synchronized void decreaseStock() {
        this.stock = this.stock.subtract(BigDecimal.valueOf(1));
    }

}
