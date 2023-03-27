package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.Order.OrderStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class OrderResponseDTO {
    private String id;
    private Instant created;
    private Instant cancellableUntil;
    private String deliveryAddress;
    private Number price;
    private UserResponseDTO user;
    private Set<ProductOrderDTO> productOrders;
    private OrderStatus status;
}
