package com.diploma.gazon.models.Product;

import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.PhotoLimitExceededException;
import com.diploma.gazon.models.User.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Data
@Document(collection = "products")
@Builder
public class Product {
    private static final Integer IMAGE_LIMIT = 5;

    @Id
    private String id;
    private String name;
    private String description;

    private Number price;
    private Boolean isInStock;

    private Float rating;
    private Set<Review> reviews;

    @DBRef
    private User user;

    private Set<String> tags;
    private Set<String> images;

    public synchronized void addProductImage(String imageName) {
        if (images == null) {
            images = new HashSet<>();
        }

        if (images.size() >= IMAGE_LIMIT) {
            throw new PhotoLimitExceededException("Достигнут лимит фотографий для данного продукта");
        }

        this.images.add(imageName);
    }

    public synchronized void removeProductImage(String imageName) {
        this.images.remove(imageName);
    }

    public synchronized void addReview(Review review) {
        if (reviewExistsByUsername(review.getUserUsername())) {
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

        String newBody = body != null ? body : review.getBody();
        Float newRating = rating != null ? rating : review.getRating();

        changeReviewOfUser(review, newBody, newRating);
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
                .filter(o -> Objects.equals(o.getUser().getUsername(), username))
                .findFirst();
    }

    public synchronized void toggleInStockStatus() {
        this.isInStock = !this.isInStock;
    }

    public static class ProductBuilder {
        private Boolean isInStock = true;
        private Float rating = 0F;
        private Set<Review> reviews = new HashSet<>();
        private Set<String> images = new HashSet<>();
    }
}
