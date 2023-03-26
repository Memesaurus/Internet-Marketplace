package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.DTO.request.ReviewDTO;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import com.diploma.gazon.services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductReviewService extends ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;

    public Set<Review> getReviewsOfProduct(String productId) {
        Product product = getOrElseThrow(productId);

        return product.getReviews();
    }

    public void addReviewToProduct(String productId, ReviewDTO reviewDTO) {
        Product product = getOrElseThrow(productId);
        User currentUser = userService.getCurrentUser();

        if (!currentUser.isAdmin() && isUserCompany(currentUser)) {
            throw new NotCompanyException("Писать отзывы на продукты могут только пользователи");
        }

        Review newReview = new Review(
                reviewDTO.getRating(),
                reviewDTO.getBody(),
                currentUser
        );

        product.addReview(newReview);

        productRepository.save(product);
    }

    public void patchReview(String productId, ReviewDTO reviewDTO) {
        Product product = getOrElseThrow(productId);
        User currentUser = userService.getCurrentUser();

        if (!product.getOwner().equals(currentUser) && !currentUser.isAdmin()) {
            throw new UnauthorizedException();
        }

        product.changeReviewOfUser(currentUser, reviewDTO.getBody(), reviewDTO.getRating());
        productRepository.save(product);
    }

}
