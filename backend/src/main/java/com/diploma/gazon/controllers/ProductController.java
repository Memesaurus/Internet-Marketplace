package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.request.ProductDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTOHomePage;
import com.diploma.gazon.services.ProductServices.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public List<ProductResponseDTOHomePage> getAllProductsSummary() {
        return productService.getProductListHomePage();
    }

    @GetMapping("/user/{username}")
    public List<ProductResponseDTO> getProductsOfUser(@PathVariable String username) {
        return productService.getProductsOfUser(username);
    }

    @GetMapping("/{productId}")
    public ProductResponseDTO getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    public void addProduct(@RequestBody ProductDTO productDTO) {
        productService.addProduct(productDTO);
    }

}
