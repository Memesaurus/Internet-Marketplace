package com.diploma.gazon.mappers;

import com.diploma.gazon.DTO.response.ReviewResponseDTO;
import com.diploma.gazon.models.Product.Review;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewResponseDTO toReviewResponseDto(Review review);

    Set<ReviewResponseDTO> toReviewResponseDto(Set<Review> reviews);
}
