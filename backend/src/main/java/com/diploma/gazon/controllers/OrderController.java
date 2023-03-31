package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.request.CartDTO;
import com.diploma.gazon.DTO.response.OrderResponseDTO;
import com.diploma.gazon.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponseDTO placeOrder(@RequestBody CartDTO cartDTO) {
        return orderService.placeOrder(cartDTO);
    }

    @PostMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/deliver")
    public void deliverOrder(@PathVariable String orderId) {
        orderService.deliverOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user")
    public List<OrderResponseDTO> getOrdersByUser() {
        return orderService.getAllOrdersOfCurrentUser();
    }
}
