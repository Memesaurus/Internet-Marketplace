package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.services.ProductServices.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/product/{productId}/reviews")
@CrossOrigin
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping()
    public Set<Review> getReviewsOfProduct(@PathVariable String productId) {
        return productReviewService.getReviewsOfProduct(productId);
    }

    @PostMapping()
    public void addReview(@PathVariable String productId, @RequestBody ReviewDTO reviewDTO) {
        productReviewService.addReviewToProduct(productId, reviewDTO);
    }

    @PatchMapping()
    public void patchReview(@PathVariable String productId, @RequestBody ReviewDTO reviewDTO) {
        productReviewService.patchReview(productId, reviewDTO);

    }
}
