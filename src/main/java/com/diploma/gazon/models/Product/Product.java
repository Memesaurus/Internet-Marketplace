package com.diploma.gazon.models.Product;

import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;

    private BigDecimal price;
    private Boolean isInStock;

    private Float rating;
    private List<Review> reviews;

    @DBRef
    private CompanyMember owner;

    private List<String> tags;

    public Product(
            String name,
            String description,
            BigDecimal price,
            List<String> tags,
            CompanyMember owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tags = tags;
        this.owner = owner;
        this.isInStock = true;
        this.rating = 0F;
        this.reviews = new ArrayList<>();
    }

    public synchronized void addReview(Review review, User user) {
        if (Boolean.TRUE.equals(reviewExistsByUsername(user.getUsername()))) {
            throw new AlreadyExistsException("User has already reviewed this product");
        }

        this.reviews.add(review);
        calculateRating();
    }

    public Boolean reviewExistsByUsername(String username) {
        return findReviewByUsername(username).isPresent();
    }

    public synchronized void changeReview(User user, String message, Float rating) {
        Review review = getReviewByUsername(user.getUsername());

        changeReview(review, message, rating);
    }

    public synchronized void changeReview(User user, String message) {
        Review review = getReviewByUsername(user.getUsername());

        changeReview(review, message, review.getRating());
    }

    public synchronized void changeReview(User user, Float rating) {
        Review review = getReviewByUsername(user.getUsername());

        changeReview(review, review.getMessage(), rating);
    }

    private synchronized void changeReview(Review review, String message, Float rating) {
        Float previousRating = review.getRating();

        review.setMessage(message);
        review.setRating(rating);

        if (!Objects.equals(rating, previousRating)) {
            calculateRating();
        }
    }

    private synchronized void calculateRating() {
        Float ratingSum = reviews.stream()
                .map(Review::getRating)
                .reduce(Float::sum)
                .orElse(0F);

        this.rating = ratingSum / reviews.size();
    }

    public Review getReviewByUsername(String username) {
        return findReviewByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    private Optional<Review> findReviewByUsername(String username) {
        return reviews.stream()
                .filter(o -> Objects.equals(o.getAuthor().getUsername(), username))
                .findFirst();
    }

    public synchronized void changeInStockStatus() {
        this.isInStock = !this.isInStock;
    }

}
