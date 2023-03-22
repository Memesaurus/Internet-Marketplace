package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts() {
        return productService.getProductList();
    }

    @GetMapping("/{username}")
    public List<Product> getProductsOfUser(@PathVariable String username) {
        return productService.getProductsOfUser(username);
    }

    @PostMapping
    public void addProduct(@RequestBody ProductDTO productDTO) {
        productService.addProduct(productDTO);
    }

    @PostMapping("/{productId}/images")
    public void addImage(@PathVariable String productId, @RequestParam("image") MultipartFile image) throws IOException {
        productService.addImage(productId, image);
    }

    @GetMapping(value = "/{productId}/images/{imageTitle}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getProductImages(@PathVariable String productId, @PathVariable String imageTitle) {
        return productService.getProductImage(productId, imageTitle);
    }

    @GetMapping("/{productId}/reviews")
    public Set<Review> getReviewsOfProduct(@PathVariable String productId) {
        return productService.getReviewsOfProduct(productId);
    }

    @PostMapping("/{productId}/reviews")
    public void addReview(@PathVariable String productId, @RequestBody ReviewDTO reviewDTO) {
        productService.addReviewToProduct(productId, reviewDTO);
    }

    @PatchMapping("/{productId}/reviews")
    public void patchReview(@PathVariable String productId, @RequestBody ReviewDTO reviewDTO) {
        productService.patchReview(productId, reviewDTO);
    }
}
