package com.diploma.gazon.services;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService<User> userService;

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public List<Product> getProductsOfUser(String username) {
        User user = userService.getUserByUsername(username);

        return productRepository.findAllByOwnerId(user.getId());
    }

    public void addProduct(ProductDTO productDTO) {
        User currentUser = userService.getCurrentUser();

        if (Boolean.FALSE.equals(isUserCompany(currentUser))) {
            throw new NotCompanyException("Выставлять продукты на сервис могут только компании");
        }

        // TODO add mapstruct to project
        Product product = new Product(
                productDTO.name,
                productDTO.description,
                productDTO.price,
                productDTO.tags,
                (CompanyMember) currentUser
        );

        productRepository.save(product);
    }

    private Boolean isUserCompany(User user) {
        User userFromDB = userService.getUserByUsername(user.getUsername());

        return  userService.isNewUserRoleAdmin(user.getUserRole()) || userFromDB instanceof CompanyMember;
    }

}
