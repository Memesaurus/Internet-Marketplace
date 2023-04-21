package com.diploma.gazon.mappers;

import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTOHomePage;
import com.diploma.gazon.models.Product.Product;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toProductResponseDto(Product product);

    List<ProductResponseDTO> toProductResponseDto(Iterable<Product> products);

    Set<ProductResponseDTO> toProductResponseDto(HashSet<Product> product);

    List<ProductResponseDTOHomePage> toProductResponseDtoHomePage(Iterable<Product> products);
}
