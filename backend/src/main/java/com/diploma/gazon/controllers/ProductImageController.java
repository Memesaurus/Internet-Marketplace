package com.diploma.gazon.controllers;

import com.diploma.gazon.services.ProductServices.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products/{productId}/images")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductImageController {
    @Autowired
    private ProductImageService productImageService;

    @PostMapping()
    public void addImage(@PathVariable String productId, @RequestParam("image") MultipartFile image) {
        productImageService.addImage(productId, image);
    }

    @GetMapping(value = "/{imageId}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[]
    getProductImages(@PathVariable String productId, @PathVariable String imageId) {
        return productImageService.getProductImage(productId, imageId);
    }

}
