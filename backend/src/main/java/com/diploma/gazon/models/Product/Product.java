package com.diploma.gazon.models.Product;

import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.PhotoLimitExceededException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.User.User;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.*;

@Data
@Document(collection = "products")
public class Product {
    private static final Integer IMAGE_LIMIT = 5;

    @Id
    private String id;
    private String name;
    private String description;

    private BigDecimal price;
    private Boolean isInStock;

    private Float rating;
    private Set<Review> reviews;

    @DBRef
    private User owner;

    private Set<String> tags;
    private Set<String> images;

    public Product(
            String name,
            String description,
            BigDecimal price,
            Set<String> tags,
            User owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tags = tags;
        this.owner = owner;
        this.isInStock = true;
        this.rating = 0F;
        this.reviews = new HashSet<>();
        this.images = new HashSet<>();
    }

    public synchronized void addProductImage(String imageName) {
        if (images.size() >= IMAGE_LIMIT) {
            throw new PhotoLimitExceededException("Достигнут лимит фотографий для данного продукта");
        }

        this.images.add(imageName);
    }

    public synchronized void addReview(Review review) {
        if (reviewExistsByUsername(review.getAuthorUsername())) {
            throw new AlreadyExistsException("Пользователь уже написал отзыв на этот продукт");
        }

        this.reviews.add(review);
        calculateRating();
    }

    public Boolean reviewExistsByUsername(String username) {
        return findReviewByUsername(username).isPresent();
    }

    public synchronized void changeReviewOfUser(User user, String body, Float rating) {
        Review review = getReviewByUsername(user.getUsername());

        String newBody = defaultIfNull(body, review.getBody());
        Float newRating = defaultIfNull(rating, review.getRating());

        changeReviewOfUser(review, newBody, newRating);
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private synchronized void changeReviewOfUser(Review review, String message, Float rating) {
        Float previousRating = review.getRating();

        review.setBody(message);
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
