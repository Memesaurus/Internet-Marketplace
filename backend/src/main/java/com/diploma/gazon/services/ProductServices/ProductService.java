package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.DTO.request.ProductDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.mappers.ProductMapper;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import com.diploma.gazon.services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;

    public List<ProductResponseDTO> getProductList() {
        List<Product> products = productRepository.findAll();

        return productMapper.toProductResponseDto(products);
    }

    public List<ProductResponseDTO> getProductsOfUser(String username) {
        User user = userService.getUserByUsername(username);

        List<Product> products = productRepository.findAllByOwnerId(user.getId());

        return productMapper.toProductResponseDto(products);
    }

    public void addProduct(ProductDTO productDTO) {
        User currentUser = userService.getCurrentUser();

        if (!currentUser.isAdmin() && !isUserCompany(currentUser)) {
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

    public Product getOrElseThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
    }
}
