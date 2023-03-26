package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.request.CartDTO;
import com.diploma.gazon.models.Order.Order;
import com.diploma.gazon.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestBody CartDTO cartDTO) {
        return orderService.placeOrder(cartDTO);
    }

    @PostMapping("/{orderId}/cancel")
    public void cancelOrder(@RequestParam String orderId) {
        orderService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/deliver")
    public void deliverOrder(@RequestParam String orderId) {
        orderService.deliverOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user")
    public List<Order> getOrderByUser() {
        return orderService.getAllOrdersOfUser();
    }
}
