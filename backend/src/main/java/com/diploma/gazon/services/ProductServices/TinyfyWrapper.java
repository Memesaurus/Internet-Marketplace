package com.diploma.gazon.services.ProductServices;

import com.diploma.gazon.exceptions.AppException;
import com.diploma.gazon.exceptions.TinyfyApiExceptions.TinifyApiException;
import com.tinify.AccountException;
import com.tinify.ServerException;
import com.tinify.Tinify;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface TinyfyWrapper {
    static void saveImage(MultipartFile image, Path newFile) throws TinifyApiException {
        try {
            Tinify.fromBuffer(image.getBytes()).toFile(newFile.toString());
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при сохранении изображения");
        } catch (ServerException | AccountException e) {
            throw new TinifyApiException("Error compressing image");
        }
    }
}
