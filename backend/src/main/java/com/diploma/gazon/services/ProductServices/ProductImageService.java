package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.exceptions.ImageException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.TinyfyApiExceptions.TinifyApiException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.models.Product.Product;
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
import java.util.stream.Stream;

@Service
public class ProductImageService {
    public static final String UPLOADS_PATH = "src/main/resources/static/uploads/";

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;

    public void addImage(String productId, MultipartFile image) {
        Product product = productService.getOrElseThrow(productId);

        if (isNotAuthorized(product)) {
            throw new UnauthorizedException();
        }

        Path dir = getOrCreateProductDir(productId);
        String imageName = ObjectId.get().toString();

        Path imagePath = Paths.get(dir.toString(), imageName + ".png");

        try {
            TinyfyWrapper.saveImage(image, imagePath);
        } catch (TinifyApiException e) {
            tryManualSave(image, imagePath);
        }


        product.addProductImage(imageName);
        productRepository.save(product);
    }

    private void tryManualSave(MultipartFile image, Path newFile) {
        try {
            Files.write(newFile, image.getBytes());
        } catch (IOException e) {
            throw new ImageException("Ошибка при загрузке изображения");
        }
    }

    public void removeImage(String productId, String imageName) {
        Product product = productService.getOrElseThrow(productId);

        if (isNotAuthorized(product)) {
            throw new UnauthorizedException();
        }

        Path dir = getOrCreateProductDir(productId);
        Path imagePath = getImagePathFromDir(dir, imageName);

        try {
            Files.delete(imagePath);
        } catch (IOException ex) {
            throw new ImageException("Ошибка при удалении изображения");
        }

        product.removeProductImage(imageName);

        productRepository.save(product);
    }

    private boolean isNotAuthorized(Product product) {
        User currentUser = userService.getCurrentUser();

        return !product.getOwner().equals(currentUser) && !currentUser.isAdmin();
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
        return Paths.get(UPLOADS_PATH + productId);
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
