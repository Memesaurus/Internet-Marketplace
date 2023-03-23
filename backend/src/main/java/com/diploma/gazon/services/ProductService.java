package com.diploma.gazon.services;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.exceptions.AppException;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import com.diploma.gazon.services.UserService.UserService;
import com.tinify.AccountException;
import com.tinify.ServerException;
import com.tinify.Tinify;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public Set<Review> getReviewsOfProduct(String productId) {
        Product product = getOrElseThrow(productId);

        return product.getReviews();
    }

    public void addProduct(ProductDTO productDTO) {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAdmin(currentUser) && !isUserCompany(currentUser)) {
            throw new NotCompanyException("Выставлять продукты на сервис могут только компании");
        }

        // TODO add mapstruct to project
        Product product = new Product(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getTags(),
                currentUser
        );

        productRepository.save(product);
    }

    public void addReviewToProduct(String productId, ReviewDTO reviewDTO) {
        Product product = getOrElseThrow(productId);
        User currentUser = userService.getCurrentUser();

        if (isUserNotAdmin(currentUser) && isUserCompany(currentUser)) {
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

    private Boolean isUserCompany(User user) {
        User userFromDB = userService.getUserByUsername(user.getUsername());

        return userFromDB instanceof CompanyMember;
    }

    private Boolean isUserNotAdmin(User user) {
        return !userService.isUserRoleAdmin(user.getUserRole());
    }

    public void patchReview(String productId, ReviewDTO reviewDTO) {
        Product product = getOrElseThrow(productId);
        User currentUser = userService.getCurrentUser();

        product.changeReviewOfUser(currentUser, reviewDTO.getBody(), reviewDTO.getRating());
        productRepository.save(product);
    }

    private Product getOrElseThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
    }

    public void addImage(String productId, MultipartFile image) {
        Product product = getOrElseThrow(productId);
        File dir = getOrCreateProductDir(productId);
        String imageName = ObjectId.get().toString();
        File newImage = new File(dir.getPath(), imageName + ".png");

        try {
            Tinify.fromBuffer(image.getBytes()).toFile(newImage.getPath());
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при загрузке изображения");
        } catch (ServerException | AccountException e) {
            // If api returns server error or api compression limit is exceeded, try saving without compressing
            try {
                Files.write(newImage.toPath(), image.getBytes());
            } catch (IOException ex) {
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при загрузке изображения");
            }
        }

        product.addProductImage(imageName);
        productRepository.save(product);
    }

    public void removeImage(String productId, String imageName) {
        //TODO remove image and remove image name from entity set
    }

    public byte[] getProductImage(String productId, String fileName) {
        File dir = getOrCreateProductDir(productId);
        File image = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> file.getName().equals(fileName))
                .findFirst()
                .orElseThrow(NotFoundException::new);

        return parseImage(image);

    }

    private byte[] parseImage(File image) {
        try {
            return Files.readAllBytes(image.toPath());
        } catch (IOException exception) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading image");
        }
    }

    private File getOrCreateProductDir(String productId) {
        File productDirectory = getProductDirectory(productId);

        if (!productDirectory.exists()) {
            productDirectory.mkdirs();
        }

        return productDirectory;
    }

    @NotNull
    private File getProductDirectory(String productId) {
        return new File("src/main/resources/static/uploads/" + productId);
    }

}
