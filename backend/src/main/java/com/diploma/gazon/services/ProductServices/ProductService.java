package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.exceptions.ImageException;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.TinyfyApiExceptions.TinifyApiException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import com.diploma.gazon.services.UserServices.UserService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public List<Product> getProductsOfUser(String username) {
        User user = userService.getUserByUsername(username);

        return productRepository.findAllByOwnerId(user.getId());
    }

    public void addProduct(ProductDTO productDTO) {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAdmin(currentUser) && !isUserCompany(currentUser)) {
            throw new NotCompanyException("Выставлять продукты на сервис могут только компании");
        }

        // TODO add mapstruct to project
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .tags(productDTO.getTags())
                .owner(currentUser)
                .build();

        productRepository.save(product);
    }

    public Boolean isUserCompany(User user) {
        User userFromDB = userService.getUserByUsername(user.getUsername());

        return userFromDB instanceof CompanyMember;
    }

    public Boolean isUserNotAdmin(User user) {
        return !userService.isUserRoleAdmin(user.getUserRole());
    }

    public Product getOrElseThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
    }
}
