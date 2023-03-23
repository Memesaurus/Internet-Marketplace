package com.diploma.gazon.services.ProductService;

import com.diploma.gazon.DTO.ProductDTO;
import com.diploma.gazon.DTO.ReviewDTO;
import com.diploma.gazon.exceptions.ImageException;
import com.diploma.gazon.exceptions.NotCompanyException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.TinyPNGApi.TinifyApiException;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.Product.Review;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.ProductRepository;
import com.diploma.gazon.services.UserService.UserService;
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

    public void addImage(String productId, MultipartFile image) {
        Product product = getOrElseThrow(productId);
        Path dir = getOrCreateProductDir(productId);
        String imageName = ObjectId.get().toString();

        Path imagePath = Paths.get(dir.toString(), imageName);

        try {
            TinyfyWrapper.saveImage(image, imagePath);
        } catch (TinifyApiException e) {
            tryManualSave(image, imagePath);
        }


        product.addProductImage(imageName);
        productRepository.save(product);
    }

    private Product getOrElseThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
    }

    private void tryManualSave(MultipartFile image, Path newFile) {
        try {
            Files.write(newFile, image.getBytes());
        } catch (IOException e) {
            throw new ImageException("Ошибка при загрузке изображения");
        }
    }

    public void removeImage(String productId, String imageName) {
        Path dir = getOrCreateProductDir(productId);
        Path imagePath = getImagePathFromDir(dir, imageName);

        try {
            Files.delete(imagePath);
        } catch (IOException ex) {
            throw new ImageException("Ошибка при удалении изображения");
        }

        Product product = getOrElseThrow(productId);
        product.removeProductImage(imageName);

        productRepository.save(product);
    }

    public byte[] getProductImage(String productId, String imageName) {
        Path dir = getOrCreateProductDir(productId);
        Path imagePath = getImagePathFromDir(dir, imageName);

        return parseImage(imagePath);

    }

    private Path getOrCreateProductDir(String productId) {
        Path productDirectory = getProductDirectory(productId);

        if (Files.notExists(productDirectory)) {
            try {
                Files.createDirectories(productDirectory);
            } catch (IOException e) {
                throw new ImageException("Ошибка при создании директории");
            }
        }

        return productDirectory;
    }

    @NotNull
    private Path getProductDirectory(String productId) {
        return Paths.get("src/main/resources/static/uploads/" + productId);
    }

    private Path getImagePathFromDir(Path dir, String imageName) {
        try (Stream<Path> stream = Files.walk(dir)) {
            return stream
                    .filter(path -> path.getFileName().toString().equals(imageName))
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
        } catch (IOException e) {
            throw new ImageException("Ошибка при загрузке изображения с сервера");
        }
    }

    private byte[] parseImage(Path image) {
        try {
            return Files.readAllBytes(image);
        } catch (IOException exception) {
            throw new ImageException("Ошибка при загрузке изображения с сервера");
        }
    }

}
