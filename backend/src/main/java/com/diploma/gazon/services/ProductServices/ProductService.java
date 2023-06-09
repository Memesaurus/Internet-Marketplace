package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.DTO.request.ProductDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTOHomePage;
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

    public List<ProductResponseDTOHomePage> getProductListHomePage() {
        List<Product> products = productRepository.findAll();

        return productMapper.toProductResponseDtoHomePage(products);
    }

    public ProductResponseDTO getProduct(String productId) {
        Product product = getOrElseThrow(productId);

        return productMapper.toProductResponseDto(product);
    }

    public List<ProductResponseDTO> getProductsOfUser(String username) {
        User user = userService.getUserByUsername(username);

        List<Product> products = productRepository.findAllByUserId(user.getId());

        return productMapper.toProductResponseDto(products);
    }

    public String addProduct(ProductDTO productDTO) {
        User currentUser = userService.getCurrentUser();

        if (!currentUser.isAdmin() && !isUserCompany(currentUser)) {
            throw new NotCompanyException("Выставлять продукты на сервис могут только компании");
        }

        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .tags(productDTO.getTags())
                .user(currentUser)
                .build();

        productRepository.save(product);

        return product.getId();
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
