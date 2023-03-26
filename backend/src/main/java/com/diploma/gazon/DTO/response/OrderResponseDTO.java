package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.Order.OrderStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class OrderResponseDTO {
    private String id;
    private Instant created;
    private Instant cancellableUntil;
    private String deliveryAddress;
    private Number price;
    private UserResponseDTO user;
    private Map<ProductResponseDTO, Number> productQuantityMap;
    private OrderStatus status;
}
