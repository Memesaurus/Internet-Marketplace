package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
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

    @PostMapping("/{id}")
    public void addReview(@RequestBody ReviewDTO reviewDTO) {
        //PlaceHolder
    }
}
