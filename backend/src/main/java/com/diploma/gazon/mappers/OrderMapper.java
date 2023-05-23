package com.diploma.gazon.mappers;

import com.diploma.gazon.DTO.response.OrderResponseDTO;
import com.diploma.gazon.DTO.response.ProductResponseDTO;
import com.diploma.gazon.models.Order.Order;
import com.diploma.gazon.models.Product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDTO toOrderResponseDto(Order order);

    List<OrderResponseDTO> toOrderResponseDto(Iterable<Order> orders);
}
